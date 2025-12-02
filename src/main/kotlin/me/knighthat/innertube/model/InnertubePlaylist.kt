package me.knighthat.innertube.model

import me.knighthat.innertube.response.Runs


interface InnertubePlaylist: InnertubeItem, Descriptive, AccessibleViaUrl, Continued {

    /**
     * Usually contains:
     *
     * - View count
     * - Total number of songs
     * - Total duration
     * - Or all above
     */
    val subtitle: Runs?

    val subtitleText: String?

    val songs: List<InnertubeSong>

    val songContinuation: String?
}