package me.knighthat.innertube.model

import me.knighthat.innertube.response.Continuation
import me.knighthat.innertube.response.Runs


interface InnertubePlaylist: InnertubeItem, Descriptive {

    /**
     * Usually contains:
     *
     * - View count
     * - Total number of songs
     * - Total duration
     * - Or all above
     */
    val subtitle: Runs?

    val continuations: List<Continuation>

    val songs: List<InnertubeSong>

    val songContinuation: String?

    val visitorData: String?
}