package me.knighthat.innertube.model

import me.knighthat.innertube.response.Runs


interface InnertubeSearchSuggestion {

    /**
     * This is text-based suggestions (or completion)
     */
    val suggestions: List<Suggestion>

    /**
     * Suggested items that could be matches for input
     */
    val items: List<InnertubeItem>

    interface Suggestion {

        val text: Runs

        val query: String
    }
}