package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.response.BrowseResponse.Contents.TwoColumnBrowseResultsRenderer
import me.knighthat.innertube.response.Continuation
import me.knighthat.innertube.response.MusicTwoRowItemRenderer
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.SectionListRenderer
import me.knighthat.innertube.response.Thumbnails
import java.util.Objects

@Serializable
internal data class InnertubePlaylistImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val description: String?,
    override var subtitle: Runs?,
    override var continuations: List<Continuation>,
    override var songs: List<InnertubeSong>,
    override var songContinuation: String?,
    override var visitorData: String?
): InnertubePlaylist {

    override val subtitleText: String? by lazy { subtitle?.runs?.joinToString( "" ) { it.text } }
}
