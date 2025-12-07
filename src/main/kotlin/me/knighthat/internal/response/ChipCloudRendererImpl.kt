package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.ChipCloudRenderer

@Serializable
internal data class ChipCloudRendererImpl(
    override val chips: List<ChipImpl> = emptyList()
): ChipCloudRenderer {

    @Serializable
    internal data class ChipImpl(
        override val chipCloudChipRenderer: RendererImpl
    ): ChipCloudRenderer.Chip {

        @Serializable
        internal data class RendererImpl(
            override val text: RunsImpl?,
            override val navigationEndpoint: EndpointImpl,
            override val onDeselectedCommand: EndpointImpl?
        ): ChipCloudRenderer.Chip.Renderer
    }
}