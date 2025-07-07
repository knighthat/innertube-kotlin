package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
import me.knighthat.innertube.response.MusicTwoRowItemRenderer
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.Thumbnails

@Serializable
internal data class InnertubeAlbumImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val isExplicit: Boolean,
    override val artists: List<Runs.Run>,
    override val year: String
): InnertubeAlbum {

    companion object {

        private val YEAR_REGEX = Regex("\\d{4}")

        private val Runs.year: String
            get() = runs.firstOrNull { it.text.matches( YEAR_REGEX ) }?.text.orEmpty()

        fun from( renderer: MusicTwoRowItemRenderer ): InnertubeAlbum {
            val run = renderer.title.runs.first()       // Requires not null to proceed
            val subtitle = renderer.subtitle

            return InnertubeAlbumImpl(
                // [id] must be a non-null value
                run.navigationEndpoint!!.browseEndpoint!!.browseId,
                run.text,
                renderer.thumbnailRenderer.toThumbnailList(),
                renderer.subtitleBadges.containsExplicitBadge,
                subtitle.extractArtistAndAlbum().artists,
                subtitle.year
            )
        }

        fun from( renderer: MusicResponsiveListItemRenderer ): InnertubeAlbum {
            val columns = renderer.flexColumns
            // Requires at least 2 columns, 1 for title, and 1 for artist(s) (and maybe release year)
            assert( columns.size >= 2 )

            val flexColumns = columns[1].musicResponsiveListItemFlexColumnRenderer!!.text!!

            return InnertubeAlbumImpl(
                // [id] must not be null in any circumstances
                renderer.navigationEndpoint!!.browseEndpoint!!.browseId,
                columns.first().musicResponsiveListItemFlexColumnRenderer!!.text!!.firstText,
                renderer.thumbnail.toThumbnailList(),
                renderer.badges.containsExplicitBadge,
                flexColumns.extractArtistAndAlbum().artists,
                flexColumns.year
            )
        }
    }
}
