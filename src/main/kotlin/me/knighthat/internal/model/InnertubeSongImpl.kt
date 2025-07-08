package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.PageType
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
import me.knighthat.innertube.response.MusicTwoRowItemRenderer
import me.knighthat.innertube.response.PlaylistPanelRenderer
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.Thumbnails


@Serializable
internal data class InnertubeSongImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val isExplicit: Boolean,
    override val durationText: String?,
    override val album: Runs.Run?,
    override val artists: List<Runs.Run>,
    override val artistsText: String
): InnertubeSong {

    companion object {

        /**
         * Matches:
         * - 0:12
         * - 12:23
         * - 1:02:03
         */
        private val DURATION_REGEX = Regex( "^(?:(\\d+):)?(\\d{1,2}):(\\d{2})$" )

        private val List<Runs.Run>.duration: String?
            get() = map( Runs.Run::text ).firstOrNull { it.matches( DURATION_REGEX ) }

        /**
         * Extract duration text from both columns
         *
         * @return human-readable format of duration, empty string if non found
         */
        private fun extractDuration( renderer: MusicResponsiveListItemRenderer ): String? {
            val merged = (renderer.flexColumns + renderer.fixedColumns).mapNotNull {
                it.musicResponsiveListItemFlexColumnRenderer ?: it.musicResponsiveListItemFixedColumnRenderer
            }

            return merged.mapNotNull( MusicResponsiveListItemRenderer.Colum.Renderer::text )
                         .flatMap( Runs::runs )
                         .duration
        }

        fun from( renderer: PlaylistPanelRenderer.Content.VideoRenderer ): InnertubeSong {
            val artists = ArrayList<Runs.Run>()
            var album: Runs.Run? = null

            renderer.longBylineText.runs.forEach {  run ->
                when( run.navigationEndpoint.pageType ) {
                    PageType.ALBUM  -> album = run
                    PageType.ARTIST -> artists.add( run )
                    else            -> { /* Does nothing */ }
                }
            }

            return InnertubeSongImpl(
                renderer.videoId,
                renderer.title.firstText,
                renderer.thumbnail.thumbnails,
                renderer.badges.containsExplicitBadge,
                renderer.lengthText.firstText,
                album,
                artists,
                renderer.shortBylineText.firstText
            )
        }

        fun from( renderer: MusicResponsiveListItemRenderer ): InnertubeSong {
            var id: String? = null
            var title = ""
            val artists = ArrayList<Runs.Run>()
            var album: Runs.Run? = null


            val columns: List<Runs> = renderer.flexColumns
                                              .mapNotNull { it.musicResponsiveListItemFlexColumnRenderer?.text }

            for( run in columns.flatMap { it.runs } ) {
                val navEndpoint = run.navigationEndpoint ?: continue

                if( navEndpoint.watchEndpoint != null ) {
                    title = run.text
                    id = navEndpoint.watchEndpoint!!.videoId
                    continue
                }

                when( run.navigationEndpoint.pageType ) {
                    PageType.ALBUM  -> album = run
                    PageType.ARTIST -> artists.add( run )
                    else            -> {}
                }
            }

            if(title.isBlank()) {
                // Assume first column is song's title
                title = columns.firstOrNull()?.firstText.orEmpty()
            }
            if(artists.isEmpty()) {
                // Assume second column is song's artists
                columns.getOrNull( 1 )?.also {
                    artists.addAll( it.runs )
                }
            }

            return InnertubeSongImpl(
                // [id] must not be null under any circumstances
                id ?: renderer.playlistItemData!!.videoId!!,
                title,
                renderer.thumbnail.toThumbnailList(),
                renderer.badges.containsExplicitBadge,
                extractDuration( renderer ),
                album,
                artists
            )
        }

        fun from( renderer: MusicTwoRowItemRenderer ): InnertubeSong {
            val albumAndArtists = renderer.subtitle.extractArtistAndAlbum()

            return InnertubeSongImpl(
                renderer.navigationEndpoint.watchEndpoint!!.videoId,
                renderer.title.firstText,
                renderer.thumbnailRenderer.toThumbnailList(),
                false,              // Currently there's no json has explicit badge
                renderer.subtitle.runs.duration,
                albumAndArtists.album,
                albumAndArtists.artists
            )
        }
    }

    constructor(
        id: String,
        name: String,
        thumbnails: List<Thumbnails.Thumbnail>,
        isExplicit: Boolean,
        durationText: String?,
        album: Runs.Run?,
        artists: List<Runs.Run>
    ): this (
        id,
        name,
        thumbnails,
        isExplicit,
        durationText,
        album,
        artists,
        artists.joinToString { it.text }
    )

    override fun shareUrl( host: String ): String {
        require( host.isYouTubeHost )

        return "$host/watch?v=$id"
    }
}
