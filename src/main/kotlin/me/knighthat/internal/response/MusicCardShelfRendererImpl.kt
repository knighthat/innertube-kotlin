package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicCardShelfRenderer

@Serializable
internal data class MusicCardShelfRendererImpl(
    override val thumbnail: ThumbnailImpl,
    override val title: RunsImpl,
    override val subtitle: RunsImpl,
    override val contents: List<ContentImpl> = emptyList()
): MusicCardShelfRenderer {

    @Serializable
    internal data class ContentImpl(
        override val musicResponsiveListItemRenderer: MusicResponsiveListItemRendererImpl?
    ): MusicCardShelfRenderer.Content
}