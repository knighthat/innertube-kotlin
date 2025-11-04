package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.BrowseResponse.Contents.TwoColumnBrowseResultsRenderer
import me.knighthat.innertube.response.Continuation
import me.knighthat.innertube.response.MusicTwoRowItemRenderer
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.SectionListRenderer
import me.knighthat.innertube.response.Thumbnails

@Serializable
internal data class InnertubePlaylistImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val description: String?,
    override var subtitle: Runs?,
    override var continuations: List<Continuation>,
    override var songs: List<InnertubeSong>,
    override var songContinuation: String?,
    override var visitorData: String?
): InnertubePlaylist {

    companion object {

        fun from( renderer: MusicTwoRowItemRenderer ): InnertubePlaylist {
            val run = renderer.title.runs.first()       // Requires not null to proceed
            return InnertubePlaylistImpl(
                // [id] must be a non-null value
                run.navigationEndpoint!!.browseEndpoint!!.browseId,
                run.text,
                renderer.thumbnailRenderer.toThumbnailList(),
                null,
                null,
                emptyList(),
                emptyList(),
                null,
                null
            )
        }

        fun from( visitorData: String?, renderer: TwoColumnBrowseResultsRenderer ): InnertubePlaylist {
            // FIXME: This part isn't supposed to be nullable at all.
            //  but it's reported that this part is null when logged in on some accounts
            val headerRenderer: SectionListRenderer.Content.MusicResponsiveHeaderRenderer? =
                renderer.tabs
                        .first()
                        .tabRenderer
                        .content
                        ?.sectionListRenderer
                        ?.contents
                        ?.firstOrNull()
                        ?.musicResponsiveHeaderRenderer
            val sectionListRenderer = renderer.secondaryContents!!.sectionListRenderer
            val playlistShelfRenderer = sectionListRenderer.contents.first().musicPlaylistShelfRenderer
            val playlistId = playlistShelfRenderer!!.playlistId!!
            val continuedPlaylist = ContinuedPlaylistImpl.from( playlistShelfRenderer.contents )
            val description = headerRenderer?.description?.musicDescriptionShelfRenderer?.description?.firstText

            return InnertubePlaylistImpl(
                // Add "VL" in case it's not there
                if( playlistId.startsWith("VL") ) playlistId else "VL$playlistId",
                headerRenderer?.title?.firstText.orEmpty(),
                headerRenderer?.thumbnail.toThumbnailList(),
                description,
                headerRenderer?.secondSubtitle,      // Contains delimiters by default
                sectionListRenderer.continuations,
                continuedPlaylist.songs,
                continuedPlaylist.continuation,
                visitorData
            )
        }
    }

    override val subtitleText: String? by lazy { subtitle?.runs?.joinToString( "" ) { it.text } }

    override fun shareUrl( host: String ): String {
        require( host.isYouTubeHost )

        return "$host/playlist?list=$id"
    }
}
