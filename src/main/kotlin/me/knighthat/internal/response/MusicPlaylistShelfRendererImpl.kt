package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicPlaylistShelfRenderer
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer

@Serializable
class MusicPlaylistShelfRendererImpl(
    override val playlistId: String,
    override val contents: List<ContentImpl> = emptyList(),
    override val collapsedItemCount: Int,
    override val contentsMultiSelectable: Boolean,
    override val targetId: String
): MusicPlaylistShelfRenderer {

    @Serializable
    data class ContentImpl(
        override val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer?,
        override val continuationItemRenderer: MusicPlaylistShelfRenderer.Content.ContinuationItemRenderer?
    ): MusicPlaylistShelfRenderer.Content {

        @Serializable
        data class ContinuationItemRendererImpl(
            override val trigger: String,
            override val continuationEndpoint: MusicPlaylistShelfRenderer.Content.ContinuationItemRenderer.Continuation
        ): MusicPlaylistShelfRenderer.Content.ContinuationItemRenderer {

            @Serializable
            data class ContinuationImpl(
                override val continuationCommand: MusicPlaylistShelfRenderer.Content.ContinuationItemRenderer.Continuation.Command
            ): MusicPlaylistShelfRenderer.Content.ContinuationItemRenderer.Continuation {

                @Serializable
                data class CommandImpl(
                    override val token: String,
                    override val request: String
                ): MusicPlaylistShelfRenderer.Content.ContinuationItemRenderer.Continuation.Command
            }
        }
    }
}