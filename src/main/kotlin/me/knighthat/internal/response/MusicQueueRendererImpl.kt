package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicQueueRenderer

@Serializable
internal data class MusicQueueRendererImpl(
    override val content: ContentImpl
): MusicQueueRenderer {

    @Serializable
    internal data class ContentImpl(
        override val playlistPanelRenderer: PlaylistPanelRendererImpl
    ) : MusicQueueRenderer.Content
}
