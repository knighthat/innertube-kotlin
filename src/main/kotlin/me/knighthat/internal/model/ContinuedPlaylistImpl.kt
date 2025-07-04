package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.MusicPlaylistShelfRenderer

@Serializable
internal data class ContinuedPlaylistImpl(
    override val continuation: String?,
    override val songs: List<InnertubeSong>
): ContinuedPlaylist
