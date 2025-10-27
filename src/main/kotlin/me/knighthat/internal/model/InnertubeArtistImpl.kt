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
import me.knighthat.innertube.response.PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.Owner
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

        private fun parse( header: BrowseResponse.Header ): InnertubeItem {
            val id: String = header.musicImmersiveHeaderRenderer
                                   ?.subscriptionButton
                                   ?.subscribeButtonRenderer
                                   ?.channelId
                                   .orEmpty()
            val name = requireNotNull(
                header.musicImmersiveHeaderRenderer?.title ?: header.musicVisualHeaderRenderer?.title
            ) { "missing title in BrowseResponse.Header" }
            val thumbnails = requireNotNull(
                header.musicImmersiveHeaderRenderer?.thumbnail ?: header.musicVisualHeaderRenderer?.thumbnail
            ) { "missing thumbnails in BrowseResponse.Header" }

            return object: InnertubeItem {
                override val id: String = id
                override val name: String = name.firstText
                override val thumbnails: List<Thumbnails.Thumbnail> = thumbnails.toThumbnailList()
            }
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
                        ?.singleColumnBrowseResultsRenderer
                        ?.tabs
                        ?.firstOrNull()
                        ?.tabRenderer
                        ?.content
                        ?.sectionListRenderer
                        ?.contents
            ) { "missing contents in BrowseResponse" }

            // There are (typically) 8 sections, but description is excluded here
            val sections = ArrayList<InnertubeArtist.Section>(7)
            var description: String? = response.header?.musicImmersiveHeaderRenderer?.description?.firstText
            for( content in contents ) {
                content.musicShelfRenderer?.also { musicSection ->
                    val browse = musicSection.bottomEndpoint?.browseEndpoint
                    val songs: List<InnertubeSong> = musicSection.contents
                                                                 .mapNotNull( MusicShelfRenderer.Content::musicResponsiveListItemRenderer )
                                                                 .map( InnertubeSongImpl::from )

                    sections.add(
                        SectionImpl(musicSection.title?.firstText.orEmpty(), browse?.browseId, browse?.params, songs)
                    )
                }

                content.musicDescriptionShelfRenderer
                       ?.description
                       ?.firstText
                       ?.also {
                           if( description == null )
                               description = it
                       }

                // This section contains Albums, Single & EPs, related Artists, and Playlists.
                content.musicCarouselShelfRenderer
                       ?.let( SectionImpl::from )
                       ?.also( sections::add )
            }

            val channelId = requireNotNull(
                response.responseContext
                        .serviceTrackingParams
                        .first()
                        .params["browse_id"]
            ) { "BrowseResponse doesn't contain channelId" }
            val item = parse( response.header!! )       // Requires [BrowseResponse.Header] to be a non-null value
            val header = response.header?.musicImmersiveHeaderRenderer
            val subscribeButton = header?.subscriptionButton?.subscribeButtonRenderer

            return InnertubeArtistImpl(
                channelId,
                item.name,
                item.thumbnails,
                description,
                subscribeButton?.shortSubscriberCountText?.firstText,
                subscribeButton?.longSubscriberCountText?.firstText,
                header?.monthlyListenerCount?.firstText,
                sections
            )
        }

        fun from( renderer: Owner.Renderer ): InnertubeArtist {
            val id = requireNotNull(
                renderer.navigationEndpoint.browseEndpoint?.browseId
            ) { "Owner doesn't contain browseId" }

            return InnertubeArtistImpl(
                id = id,
                name = renderer.title.firstText,
                thumbnails = renderer.thumbnail.thumbnails,
                description = null,
                shortNumSubscribers = null,
                longNumSubscribers = renderer.subscriberCountText?.simpleText,
                shortNumMonthlyAudience = null,
                sections = emptyList()
            )
        }
    }

    override fun shareUrl( host: String ): String {
        require( host.isYouTubeHost ) {
            "$host is not a YouTube url"
        }

        return "$host/channel/$id"
    }

    @Serializable
    internal data class SectionImpl(
        override val title: String,
        override val browseId: String?,
        override val params: String?,
        override val contents: List<InnertubeItem>
    ): InnertubeArtist.Section {

        companion object {

            fun from( renderer: MusicCarouselShelfRenderer ): InnertubeArtist.Section? {
                // Skip this section because 1 or all content isn't [MusicTwoRowItemRender]
                if( renderer.contents.any { it.musicTwoRowItemRenderer == null } )
                    return null

                val sectionHeader = renderer.header.musicCarouselShelfBasicHeaderRenderer
                val run = sectionHeader.title.runs.firstOrNull()
                val browse = run?.navigationEndpoint?.browseEndpoint

                return SectionImpl(
                    title = sectionHeader.title.firstText,
                    browseId = browse?.browseId,
                    params = browse?.params,
                    contents = renderer.contents
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
        }
    }
}
