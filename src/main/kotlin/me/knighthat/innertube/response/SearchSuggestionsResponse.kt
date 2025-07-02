package me.knighthat.innertube.response


interface SearchSuggestionsResponse : InnertubeResponse {

    val contents: List<Content>

    interface Content {

        val searchSuggestionsSectionRenderer: Renderer

        interface Renderer {

            val contents: List<Content>

            interface Content {

                val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer?
                val searchSuggestionRenderer: Renderer?

                interface Renderer {

                    val suggestion: Runs
                    val navigationEndpoint: Endpoint
                }
            }
        }
    }
}