package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Overlay

@Serializable
data class OverlayImpl(
    override val musicItemThumbnailOverlayRenderer: MusicItemThumbnailOverlayRendererImpl
): Overlay {

    @Serializable
    data class MusicItemThumbnailOverlayRendererImpl(
        override val background: BackgroundImpl,
        override val contentPosition: String,
        override val displayStyle: String
    ): Overlay.MusicItemThumbnailOverlayRenderer {

        @Serializable
        data class BackgroundImpl(
            override val verticalGradient: VerticalGradientImpl
        ): Overlay.MusicItemThumbnailOverlayRenderer.Background {

            @Serializable
            data class VerticalGradientImpl(
                override val gradientLayerColors: List<String> = emptyList()
            ): Overlay.MusicItemThumbnailOverlayRenderer.Background.VerticalGradient
        }
    }
}