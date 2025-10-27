package me.knighthat.internal.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.Serializable
import me.knighthat.innertube.Innertube
import me.knighthat.innertube.PageType
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.MusicCarouselShelfRenderer
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
import me.knighthat.innertube.response.MusicShelfRenderer
import me.knighthat.innertube.response.MusicTwoRowItemRenderer
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.Thumbnails
import java.net.URI

@Serializable
internal data class InnertubeAlbumImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val isExplicit: Boolean,
    override val artists: List<Runs.Run>,
    override val year: String,
    override val urlCanonical: String?,
    override val description: String?,
    override val subtitle: String?,
    override val songs: List<InnertubeSong>,
    override val sections: List<InnertubeAlbum.Section>
): InnertubeAlbum {

    companion object {

        private val YEAR_REGEX = Regex("\\d{4}")
        private val ALBUM_PLAYLIST_ID_REGEX = Regex("(?:playlist\\?list=|list=)?(OLAK5uy_[A-Za-z0-9_-]{32}|[A-Za-z0-9_-]+)(?:&|\$)")

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
                subtitle.year,
                null,
                null,
                null,
                emptyList(),
                emptyList()
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
                flexColumns.year,
                null,
                null,
                null,
                emptyList(),
                emptyList()
            )
        }

        suspend fun from( browseId: String, localization: Localization, browseResponse: BrowseResponse ): InnertubeAlbum {
            val urlCanonical: String? = browseResponse.microformat
                                                      ?.microformatDataRenderer
                                                      ?.urlCanonical

            // This will start the job but won't block it
            // allows subsequent tasks to run while fetching for songs
            val fetchSongsJob = CoroutineScope( Dispatchers.IO ).async {
                val captured = ALBUM_PLAYLIST_ID_REGEX.find( urlCanonical.orEmpty() )?.groups?.get( 1 )?.value
                if( captured.isNullOrBlank() ) return@async emptyList()

                val playlistId = "%s%s".format( if( captured.startsWith( "VL" ) ) "" else "VL", captured )
                Innertube.browsePlaylistSongs( playlistId, localization )
                         .fold(
                             onSuccess = { it },
                             onFailure = {
                                 it.printStackTrace()
                                 emptyList()
                             }
                         )
            }
            //<editor-fold desc="Parser">
            val renderer = requireNotNull(
                browseResponse.contents
                              ?.twoColumnBrowseResultsRenderer
                              ?.tabs
                              ?.firstOrNull()
                              ?.tabRenderer
                              ?.content
                              ?.sectionListRenderer
                              ?.contents
                              ?.firstOrNull()
                              ?.musicResponsiveHeaderRenderer
            ) { "missing musicResponsiveHeaderRenderer while parsing twoColumnBrowseResultsRenderer" }
            val thumbnails = renderer.thumbnail.toThumbnailList()
            val artists = renderer.straplineTextOne?.extractArtistAndAlbum()?.artists.orEmpty()
            val description: String? = renderer.description
                                               ?.musicDescriptionShelfRenderer
                                               ?.description
                                               ?.runs
                                               ?.joinToString( "" ) { it.text }
            val subtitle: String? = renderer.secondSubtitle
                                            ?.runs
                                            ?.joinToString( "" ) { it.text }
            val sections: List<InnertubeAlbum.Section> =  browseResponse.contents
                                                                        ?.twoColumnBrowseResultsRenderer
                                                                        ?.secondaryContents
                                                                        ?.sectionListRenderer
                                                                        ?.contents
                                                                        ?.mapNotNull {
                                                                            it.musicCarouselShelfRenderer?.let( SectionImpl::from )
                                                                        }
                                                                        .orEmpty()
            //</editor-fold>

            return InnertubeAlbumImpl(
                id = browseId,
                name = renderer.title.firstText,
                thumbnails = thumbnails,
                isExplicit = renderer.subtitleBadge.containsExplicitBadge,
                artists = artists,
                year = renderer.subtitle.year,
                urlCanonical = urlCanonical,
                description = description,
                subtitle = subtitle,
                songs = fetchSongsJob.await(),
                sections = sections
            )
        }
    }

    override fun shareUrl( host: String ): String {
        require( host.isYouTubeHost ) {
            "$host is not a YouTube url"
        }
        requireNotNull( this.urlCanonical ) {
            "urlCanonical is null"
        }

        return if( !urlCanonical.startsWith( host ) ) {
            val uri = URI.create( urlCanonical )

            // Copy everything except for the host
            URI(
                uri.scheme, uri.userInfo, host, uri.port, uri.path, uri.query, uri.fragment
            ).toString()
        } else
            urlCanonical
    }

    internal data class SectionImpl(
        override val title: String?,
        override val contents: List<InnertubeItem>
    ) : InnertubeAlbum.Section {

        companion object {

            fun from( renderer: MusicShelfRenderer ): InnertubeAlbum.Section =
                SectionImpl(
                    null,       // This section usually doesn't contain title
                    renderer.contents
                            .mapNotNull( MusicShelfRenderer.Content::musicResponsiveListItemRenderer )
                            .map( InnertubeSongImpl::from )
                )

            fun from( renderer: MusicCarouselShelfRenderer ): InnertubeAlbum.Section =
                SectionImpl(
                    renderer.header.musicCarouselShelfBasicHeaderRenderer.title.firstText,
                    renderer.contents
                            .mapNotNull( MusicCarouselShelfRenderer.Content::musicTwoRowItemRenderer )
                            .filter { item ->
                                // Fool proof, in case this is not the only type exists
                                item.title.runs.any {
                                    it.navigationEndpoint?.pageType == PageType.ALBUM
                                }
                            }
                            .map( InnertubeAlbumImpl::from )
                )
        }
    }
}
