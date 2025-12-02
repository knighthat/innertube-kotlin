package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.InnertubeRankedArtist
import me.knighthat.innertube.model.Section
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
import me.knighthat.innertube.response.Thumbnails

@Serializable
internal data class InnertubeRankedArtistImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val description: String?,
    override val shortNumSubscribers: String?,
    override val longNumSubscribers: String?,
    override val shortNumMonthlyAudience: String?,
    override val sections: List<Section>,
    override val rank: String,
    override val iconType: String?
): InnertubeRankedArtist {

    companion object {

        fun from( renderer: MusicResponsiveListItemRenderer ): InnertubeRankedArtist {
            val id = requireNotNull(
                value = renderer.navigationEndpoint
                                ?.browseEndpoint
                                ?.browseId
            ) { "missing browseId from MusicResponsiveListItemRenderer" }
            val columns = renderer.flexColumns.mapNotNull(
                MusicResponsiveListItemRenderer.Colum::musicResponsiveListItemFlexColumnRenderer
            )
            val indexColumn = requireNotNull(
                renderer.customIndexColumn
                        ?.musicCustomIndexColumnRenderer
            ) { "missing musicCustomIndexColumnRenderer while parsing MusicResponsiveListItemRenderer" }

            return InnertubeRankedArtistImpl(
                id,
                columns[0].text!!.firstText,
                renderer.thumbnail.toThumbnailList(),
                null,
                columns.getOrNull( 1 )?.text?.firstText,
                null,
                null,
                emptyList(),
                indexColumn.text.firstText,
                indexColumn.icon?.iconType
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
