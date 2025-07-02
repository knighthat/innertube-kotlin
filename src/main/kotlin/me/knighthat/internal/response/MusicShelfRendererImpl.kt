package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicShelfRenderer

@Serializable
internal data class MusicShelfRendererImpl(
    override val title: RunsImpl,
    override val contents: List<ContentImpl> = emptyList(),
    override val bottomText: RunsImpl?,
    override val bottomEndpoint: EndpointImpl?,
    override val contentsMultiSelectable: Boolean?
): MusicShelfRenderer {

    @Serializable
    internal data class ContentImpl(
        override val musicResponsiveListItemRenderer: MusicResponsiveListItemRendererImpl
    ): MusicShelfRenderer.Content
}