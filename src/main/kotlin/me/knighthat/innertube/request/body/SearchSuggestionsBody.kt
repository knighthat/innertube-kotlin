package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable
import me.knighthat.innertube.request.body.search.suggestions.Builder

@Serializable
data class SearchSuggestionsBody(
    val input: String,
    override val context: Context
): RequestBody {

    companion object {

        @JvmStatic
        fun builder( context: Context ): Builder = SearchSuggestionsBodyBuilder(context)
    }
}
