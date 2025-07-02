package me.knighthat.innertube.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import me.knighthat.innertube.response.JsonParser.JSON
import me.knighthat.internal.response.SearchSuggestionsResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class SearchSuggestionsResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @Test
    fun testParser() {
        val fileName = "ytm/search_suggestions/endpoint_response.json"
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            JSON.decodeFromStream<SearchSuggestionsResponseImpl>( inStream )
        }

        Assertions.assertNotNull( result )
        Assertions.assertEquals(
            "CgtMN0FkbDFaWERfdyiJx77ABjIKCgJWThIEGgAgWQ%3D%3D",
            result!!.responseContext.visitorData
        )
        Assertions.assertEquals( 2, result.contents.size )

        val suggestionRenderers = result.contents[1].searchSuggestionsSectionRenderer.contents
        Assertions.assertEquals( 3, suggestionRenderers.size )
    }
}