package me.knighthat.innertube.model

import me.knighthat.innertube.response.Runs

interface InnertubeSong: InnertubeItem, ContentRating, AccessibleViaUrl {

    /**
     * Plain text representation of duration, in short format
     */
    val durationText: String?

    /**
     * Contains information about this song's album
     */
    val album: Runs.Run?

    /**
     * List of artists featured in this song
     */
    val artists: List<Runs.Run>

    /**
     * All authors listed in 1 line, with delimiter in between
     */
    val artistsText: String
}