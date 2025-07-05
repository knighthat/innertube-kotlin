package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.PageType
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
import me.knighthat.innertube.response.PlaylistPanelRenderer
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.Thumbnails
import java.util.Objects
import java.util.stream.Collectors


@Serializable
internal data class InnertubeSongImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val isExplicit: Boolean,
    override val durationText: String?,
    override val album: Runs.Run?,
): InnertubeSong
    override val artists: List<Runs.Run>,
    override val artistsText: String
