package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.NextResponse

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
            override val results: PrimaryResultsImpl
        ): NextResponse.Contents.TwoColumnWatchNextResults
    }

    @Serializable
    data class PlayerOverlaysImpl(
        override val playerOverlayRenderer: RendererImpl
    ): NextResponse.PlayerOverlays {

        @Serializable
        data class RendererImpl(
            override val browserMediaSession: BrowserMediaSessionImpl?
        ): NextResponse.PlayerOverlays.Renderer {

            @Serializable
            data class BrowserMediaSessionImpl(
                override val browserMediaSessionRenderer: RendererImpl
            ): NextResponse.PlayerOverlays.Renderer.BrowserMediaSession {

                @Serializable
                data class RendererImpl(
                    override val album: RunsImpl?,
                    override val thumbnailDetails: ThumbnailsImpl
                ): NextResponse.PlayerOverlays.Renderer.BrowserMediaSession.Renderer
            }
        }
    }
}