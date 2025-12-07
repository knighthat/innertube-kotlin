package me.knighthat.internal.model

import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubeSearch
import me.knighthat.innertube.response.Continuation
import me.knighthat.innertube.response.SearchResponse


data class InnertubeSearchImpl(
    override val items: List<InnertubeItem>,
    override val continuations: List<Continuation>,
    override val visitorData: String?
) : InnertubeSearch {

    companion object {

        fun from( response: SearchResponse ): InnertubeSearch {
            val renderer = requireNotNull(
                response.contents
                    .tabbedSearchResultsRenderer
                    .tabs
                    .firstOrNull()
                    ?.tabRenderer
                    ?.content
                    ?.sectionListRenderer
                    ?.contents
                    ?.firstOrNull()
                    ?.musicShelfRenderer
            ) { "SearchResponse doesn't have content" }
            val items = renderer.contents
                .mapNotNull { content ->
                    content.musicResponsiveListItemRenderer
                           ?.let( ::createInnertubeItemFrom )
                }

            return InnertubeSearchImpl(
                items = items,
                continuations = renderer.continuations,
                visitorData = response.responseContext.visitorData
            )
        }
    }
}