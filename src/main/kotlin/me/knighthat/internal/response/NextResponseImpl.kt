package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.NextResponse
import me.knighthat.innertube.response.PrimaryResults

@Serializable
internal class NextResponseImpl(
    override val contents: ContentsImpl,
    override val currentVideoEndpoint: EndpointImpl,
    override val playerOverlays: PlayerOverlaysImpl,
    override val responseContext: InnertubeResponseImpl.ContextImpl
): NextResponse {

    @Serializable
    data class ContentsImpl(
        override val singleColumnMusicWatchNextResultsRenderer: SingleColumnMusicWatchNextResultsRendererImpl?,
        override val twoColumnWatchNextResults: TwoColumnWatchNextResultsImpl?
    ): NextResponse.Contents {

        @Serializable
        data class SingleColumnMusicWatchNextResultsRendererImpl(
            override val tabbedRenderer: TabbedRendererImpl
        ): NextResponse.Contents.SingleColumnMusicWatchNextResultsRenderer {

            @Serializable
            data class TabbedRendererImpl(
                override val watchNextTabbedResultsRenderer: TabsImpl
            ): NextResponse.Contents.SingleColumnMusicWatchNextResultsRenderer.TabbedRenderer
        }

        @Serializable
        data class TwoColumnWatchNextResultsImpl(
            override val results: PrimaryResults
        ): NextResponse.Contents.TwoColumnWatchNextResults
    }

    @Serializable
    data class PlayerOverlaysImpl(
        override val playerOverlayRenderer: PlayerOverlayRendererImpl
    ): NextResponse.PlayerOverlays {

        @Serializable
        data class PlayerOverlayRendererImpl(
            override val browserMediaSession: BrowserMediaSessionImpl
        ): NextResponse.PlayerOverlays.PlayerOverlayRenderer {

            @Serializable
            data class BrowserMediaSessionImpl(
                override val browserMediaSessionRenderer: BrowserMediaSessionRendererImpl
            ): NextResponse.PlayerOverlays.PlayerOverlayRenderer.BrowserMediaSession {

                @Serializable
                data class BrowserMediaSessionRendererImpl(
                    override val album: RunsImpl,
                    override val thumbnailDetails: ThumbnailsImpl
                ): NextResponse.PlayerOverlays.PlayerOverlayRenderer.BrowserMediaSession.BrowserMediaSessionRenderer
            }
        }
    }
}