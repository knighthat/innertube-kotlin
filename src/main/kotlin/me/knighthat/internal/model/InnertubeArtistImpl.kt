package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.PageType
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.MusicCarouselShelfRenderer
import me.knighthat.innertube.response.MusicShelfRenderer
import me.knighthat.innertube.response.MusicTwoRowItemRenderer
import me.knighthat.innertube.response.Thumbnails

@Serializable
internal data class InnertubeArtistImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val description: String?,
    override val shortNumSubscribers: String?,
    override val longNumSubscribers: String?,
    override val shortNumMonthlyAudience: String?,
    override val sections: List<InnertubeArtist.Section>
): InnertubeArtist {

    companion object {

        private fun parse( renderer: MusicCarouselShelfRenderer ): InnertubeArtist.Section? {
            // Skip this section because 1 or all content isn't [MusicTwoRowItemRender]
            if( renderer.contents.any { it.musicTwoRowItemRenderer == null } )
                return null

            val sectionHeader = renderer.header.musicCarouselShelfBasicHeaderRenderer
            val run = sectionHeader.title.runs.firstOrNull()
            val browse = run?.navigationEndpoint?.browseEndpoint

            return SectionImpl(
                sectionHeader.title.firstText,
                browse?.browseId,
                browse?.params,
                renderer.contents
                        .mapNotNull( MusicCarouselShelfRenderer.Content::musicTwoRowItemRenderer )
                        .mapNotNull { itemRenderer ->
                            var result: InnertubeItem? = null

                            itemRenderer.navigationEndpoint
                                        .browseEndpoint
                                        ?.browseEndpointContextSupportedConfigs
                                        ?.browseEndpointContextMusicConfig
                                        ?.pageType
                                        ?.also { pageType ->
                                            result = when( pageType ) {
                                                PageType.ARTIST     -> from( itemRenderer )
                                                PageType.ALBUM      -> InnertubeAlbumImpl.from( itemRenderer )
                                                PageType.PLAYLIST   -> InnertubePlaylistImpl.from( itemRenderer )
                                                else                -> null
                                            }
                                        }

                            itemRenderer.navigationEndpoint
                                        .watchEndpoint
                                        ?.also {
                                            result = InnertubeSongImpl.from( itemRenderer )
                                        }

                            result
                        }
            )
        }

        fun from( renderer: MusicTwoRowItemRenderer ): InnertubeArtist {
            val run = renderer.title.runs.first()       // Requires not null to proceed

            return InnertubeArtistImpl(
                // [id] must be a non-null value
                run.navigationEndpoint!!.browseEndpoint!!.browseId,
                run.text,
                renderer.thumbnailRenderer.toThumbnailList(),
                null,
                null,
                null,
                renderer.subtitle.firstText,
                emptyList()
            )
        }

        fun from( response: BrowseResponse ): InnertubeArtist {
            val contents = requireNotNull(
                response.contents
                        .singleColumnBrowseResultsRenderer
                        ?.tabs
                        ?.firstOrNull()
                        ?.tabRenderer
                        ?.content
                        ?.sectionListRenderer
                        ?.contents
            )

            val sections = ArrayList<InnertubeArtist.Section>(8)
            var description: String? = null
            for( content in contents ) {
                content.musicShelfRenderer?.also { musicSection ->
                    val browse = musicSection.bottomEndpoint?.browseEndpoint
                    val songs: List<InnertubeSong> = musicSection.contents
                                                                 .map( MusicShelfRenderer.Content::musicResponsiveListItemRenderer )
                                                                 .map( InnertubeSongImpl::from )

                    sections.add(
                        SectionImpl(musicSection.title.firstText, browse?.browseId, browse?.params, songs)
                    )
                }

                content.musicDescriptionShelfRenderer
                       ?.description
                       ?.firstText
                       ?.also { description = it }

                // This section contains Albums, Single & EPs, related Artists, and Playlists.
                content.musicCarouselShelfRenderer
                       ?.let( ::parse )
                       ?.also( sections::add )
            }

            val header = requireNotNull(
                response.header?.musicImmersiveHeaderRenderer
            )
            val subscribeButton = requireNotNull(
                header.subscriptionButton.subscribeButtonRenderer
            )

            return InnertubeArtistImpl(
                subscribeButton.channelId,
                header.title.firstText,
                header.thumbnail.toThumbnailList(),
                description,
                subscribeButton.shortSubscriberCountText.firstText,
                subscribeButton.longSubscriberCountText.firstText,
                header.monthlyListenerCount.firstText,
                sections
            )
        }
    }

    @Serializable
    internal data class SectionImpl(
        override val title: String,
        override val browseId: String?,
        override val params: String?,
        override val contents: List<InnertubeItem>
    ): InnertubeArtist.Section
}
