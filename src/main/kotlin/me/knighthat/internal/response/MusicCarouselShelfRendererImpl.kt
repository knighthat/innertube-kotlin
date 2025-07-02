package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.*

@Serializable
internal data class MusicCarouselShelfRendererImpl(
    override val header: HeaderImpl,
    override val contents: List<ContentImpl> = emptyList(),
    override val itemSize: String,
    override val numItemsPerColumn: String
): MusicCarouselShelfRenderer {

    @Serializable
    data class HeaderImpl(
        override val musicCarouselShelfBasicHeaderRenderer: MusicCarouselShelfBasicHeaderRendererImpl
    ): MusicCarouselShelfRenderer.Header {

        @Serializable
        data class MusicCarouselShelfBasicHeaderRendererImpl(
            override val title: Runs,
            override val strapline: Runs?,
            override val accessibilityData: Accessibility?,
            override val headerStyle: String
        ): MusicCarouselShelfRenderer.Header.MusicCarouselShelfBasicHeaderRenderer
    }

    @Serializable
    data class ContentImpl(
        override val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer?,
        override val musicTwoRowItemRenderer: MusicTwoRowItemRenderer?
    ): MusicCarouselShelfRenderer.Content
}