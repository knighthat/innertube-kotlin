package me.knighthat.innertube.model

import me.knighthat.innertube.Localized

interface Descriptive {

    /**
     * Brief information
     */
    @get:Localized
    val description: String?
}