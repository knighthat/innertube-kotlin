package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.Section
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
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
    override val sections: List<Section>
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
            val sections = ArrayList<Section>(7)
            var description: String? = response.header?.musicImmersiveHeaderRenderer?.description?.firstText
            for( content in contents ) {
                content.musicShelfRenderer
                       ?.let( ::createModelSectionFrom )
                       ?.also( sections::add )

                content.musicDescriptionShelfRenderer
                       ?.description
                       ?.firstText
                       ?.also {
                           if( description == null )
                               description = it
                       }

                // This section contains Albums, Single & EPs, related Artists, and Playlists.
                content.musicCarouselShelfRenderer
                       ?.let( ::createModelSectionFrom )
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

        fun from( renderer: MusicResponsiveListItemRenderer ): InnertubeArtist {
            val id = requireNotNull(
                renderer.navigationEndpoint
                        ?.browseEndpoint
                        ?.browseId
            ) { "MusicResponsiveListItemRenderer doesn't contain id" }
            val columns = renderer.flexColumns.mapNotNull {
                it.musicResponsiveListItemFlexColumnRenderer?.text?.firstText
            }
            require( columns.isNotEmpty() ) { "MusicResponsiveListItemRenderer contains no information" }

            return InnertubeArtistImpl(
                id = id,
                name = columns.first(),
                thumbnails = renderer.thumbnail.toThumbnailList(),
                description = null,
                shortNumSubscribers = null,
                longNumSubscribers = null,
                shortNumMonthlyAudience = columns.getOrNull( 1 ),
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
}
