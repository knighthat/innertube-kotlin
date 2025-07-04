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
    var subtitle: Runs?

    var continuations: List<Continuation>

    var songs: List<InnertubeSong>

    var songContinuation: String?

    var visitorData: String?
}