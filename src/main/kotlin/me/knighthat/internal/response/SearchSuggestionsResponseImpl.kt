package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.SearchSuggestionsResponse

@Serializable
internal data class SearchSuggestionsResponseImpl(
    override val contents: List<ContentImpl> = emptyList(),
    override val responseContext: InnertubeResponseImpl.ContextImpl
): SearchSuggestionsResponse {

    @Serializable
    data class ContentImpl(
        override val searchSuggestionsSectionRenderer: RendererImpl
    ): SearchSuggestionsResponse.Content {

        @Serializable
        internal data class RendererImpl(
            override val contents: List<ContentImpl> = emptyList()
        ): SearchSuggestionsResponse.Content.Renderer {

            @Serializable
            data class ContentImpl(
                override val musicResponsiveListItemRenderer: MusicResponsiveListItemRendererImpl?,
                override val searchSuggestionRenderer: RendererImpl?
            ): SearchSuggestionsResponse.Content.Renderer.Content {

                @Serializable
                data class RendererImpl(
                    override val suggestion: RunsImpl,
                    override val navigationEndpoint: EndpointImpl
                ): SearchSuggestionsResponse.Content.Renderer.Content.Renderer
            }
        }
    }
}