package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.PageType
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.MusicCarouselShelfRenderer
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
import me.knighthat.innertube.response.MusicShelfRenderer
import me.knighthat.innertube.response.MusicTwoRowItemRenderer
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.SectionListRenderer
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

        fun from( browseId: String, browseResponse: BrowseResponse ): InnertubeAlbum {
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
            )
            val contents: List<SectionListRenderer.Content> = browseResponse.contents
                                                                            ?.twoColumnBrowseResultsRenderer
                                                                            ?.secondaryContents
                                                                            ?.sectionListRenderer
                                                                            ?.contents
                                                                            .orEmpty()

            return InnertubeAlbumImpl(
                id = browseId,
                name = renderer.title
                               .firstText,
                thumbnails = renderer.thumbnail
                                     .toThumbnailList(),
                isExplicit = renderer.subtitleBadge
                                     .containsExplicitBadge,
                artists = renderer.straplineTextOne
                                  ?.extractArtistAndAlbum()
                                  ?.artists
                                  .orEmpty(),
                year = renderer.subtitle
                               .year,
                urlCanonical = browseResponse.microformat
                                             ?.microformatDataRenderer
                                             ?.urlCanonical,
                description = renderer.description
                                      ?.musicDescriptionShelfRenderer
                                      ?.description
                                      ?.runs
                                      ?.joinToString( "" ) { it.text },
                subtitle = renderer.secondSubtitle
                                   ?.runs
                                   ?.joinToString( "" ) { it.text },
                songs = contents.mapNotNull( SectionListRenderer.Content::musicShelfRenderer )
                                .flatMap {
                                    it.contents
                                      .mapNotNull( MusicShelfRenderer.Content::musicResponsiveListItemRenderer )
                                      .map( InnertubeSongImpl::from )
                                },
                sections = contents.mapNotNull {
                    it.musicCarouselShelfRenderer?.let( SectionImpl::from )
                }
            )
        }
    }

    override fun shareUrl( host: String ): String {
        require( host.isYouTubeHost )
        requireNotNull( this.urlCanonical)

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
