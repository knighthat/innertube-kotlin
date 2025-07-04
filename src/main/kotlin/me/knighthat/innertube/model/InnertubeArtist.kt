package me.knighthat.innertube.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.Localized


interface InnertubeArtist: InnertubeItem, Descriptive {

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

    val sections: List<Section>

    @Serializable
    data class Section(
        @Localized val title: String,
        val browseId: String?,
        val params: String?,
        val contents: List<InnertubeItem>
    )
}