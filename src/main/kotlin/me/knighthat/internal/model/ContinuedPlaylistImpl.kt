package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.MusicPlaylistShelfRenderer

@Serializable
internal data class ContinuedPlaylistImpl(
    override val continuation: String?,
    override val songs: List<InnertubeSong>
): ContinuedPlaylist {

    companion object {

        fun from( items: List<MusicPlaylistShelfRenderer.Content> ): ContinuedPlaylist {
            var continuation: String? = null
            val songs = ArrayList<InnertubeSong>(items.size)

            for(item in items) {
                item.continuationItemRenderer
                    ?.continuationEndpoint
                    ?.continuationCommand
                    ?.token
                    ?.also { continuation = it }

                item.musicResponsiveListItemRenderer
                    ?.let( InnertubeSongImpl::from )
                    ?.also( songs::add )
            }

            return ContinuedPlaylistImpl(continuation, songs)
        }
    }
}
