package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.SearchResponse

@Serializable
internal data class SearchResponseImpl(
    override val contents: ContentsImpl,
    override val responseContext: InnertubeResponseImpl.ContextImpl
): SearchResponse {

    @Serializable
    internal data class ContentsImpl(
        override val tabbedSearchResultsRenderer: TabsImpl
    ): SearchResponse.Contents
}