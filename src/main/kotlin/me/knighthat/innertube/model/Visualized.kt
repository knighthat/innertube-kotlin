package me.knighthat.innertube.model

import me.knighthat.innertube.response.Thumbnails

interface Visualized {

    /**
     * A set of thumbnails, variable in sizes
     */
    val thumbnails: List<Thumbnails.Thumbnail>
}