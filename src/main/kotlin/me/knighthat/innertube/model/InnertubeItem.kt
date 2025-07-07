package me.knighthat.innertube.model

import me.knighthat.innertube.Localized
import me.knighthat.innertube.response.Thumbnails

interface InnertubeItem {

    /**
     * Unique identifier used to distinguish it from other items in a database
     */
    val id: String

    /**
     * A song's display name - the human-readable title shown to users
     */
    @get:Localized
    val name: String

    /**
     * A set of thumbnails, variable in sizes
     */
    val thumbnails: List<Thumbnails.Thumbnail>
}