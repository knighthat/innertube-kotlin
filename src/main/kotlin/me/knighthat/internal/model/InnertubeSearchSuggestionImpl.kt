package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubeSearchSuggestion
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.SearchSuggestionsResponse


@Serializable
data class InnertubeSearchSuggestionImpl(
    override val suggestions: List<InnertubeSearchSuggestion.Suggestion>,
    override val items: List<InnertubeItem>
): InnertubeSearchSuggestion {

    companion object {

        fun from( response: SearchSuggestionsResponse ): InnertubeSearchSuggestion {
            val suggestions = mutableListOf<InnertubeSearchSuggestion.Suggestion>()
            val items = mutableListOf<InnertubeItem>()

            val contents = response.contents.flatMap { content ->
                content.searchSuggestionsSectionRenderer.contents
            }
            for( content in  contents ) {
                content.searchSuggestionRenderer
                    ?.let { renderer ->
                        object : InnertubeSearchSuggestion.Suggestion {
                            override val text: Runs = renderer.suggestion
                            override val query: String =
                                renderer.navigationEndpoint.searchEndpoint?.query ?: text.runs.joinToString( "" ) { it.text }
                        }
                    }
                    ?.also( suggestions::add )

                val mrlir = content.musicResponsiveListItemRenderer ?: continue
                createInnertubeItemFrom( mrlir )?.also( items::add )
            }

            return InnertubeSearchSuggestionImpl(suggestions, items)
        }
    }
}
