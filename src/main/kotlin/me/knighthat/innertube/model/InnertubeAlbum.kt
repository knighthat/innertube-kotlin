package me.knighthat.innertube.model

import me.knighthat.innertube.response.Endpoint


interface InnertubeAlbum: InnertubeItem, ContentRating {

    /**
     * Artists featured in this album
     */
    val artists: List<Endpoint.Browse>

    /**
     * The year in which this album was released
     *
     *
     * Returns empty string if unknown
     */
    val year: String
}