package me.knighthat.innertube.model

import me.knighthat.innertube.response.Runs


interface InnertubeAlbum: InnertubeItem, ContentRating, AccessibleViaUrl, Descriptive, MultiContent {

    /**
     * Artists featured in this album
     */
    val artists: List<Runs.Run>

    /**
     * The year in which this album was released
     *
     *
     * Returns empty string if unknown
     */
    val year: String

    /**
     * This is sharable url of album
     */
    val urlCanonical: String?

    /**
     * Usually contains:
     *
     * - Total number of songs
     * - Total duration
     * - Or both
     */
    val subtitle: Runs?

    val songs: List<InnertubeSong>
}