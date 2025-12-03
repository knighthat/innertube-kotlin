package me.knighthat.innertube.model

import me.knighthat.innertube.Localized


interface InnertubeArtist: InnertubeItem, Descriptive, AccessibleViaUrl, MultiContent {

    /**
     * Number of subscribers in **short** format:
     * - 10K
     * - 1M
     *
     *
     * Supports localization
     */
    @get:Localized
    val shortNumSubscribers: String?

    /**
     * Number of subscribers in **long** format:
     * - 19.3M subscribers
     *
     *
     * Supports localization
     */
    @get:Localized
    val longNumSubscribers: String?

    /**
     * Number of monthly listeners in short format:
     * - 324M monthly audience
     *
     *
     * Supports localization
     */
    @get:Localized
    val shortNumMonthlyAudience: String?
}