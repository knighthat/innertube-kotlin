package me.knighthat.innertube.response

import kotlinx.serialization.ExperimentalSerializationApi
import me.knighthat.innertube.decode
import me.knighthat.internal.response.SearchSuggestionsResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalSerializationApi::class)
class SearchSuggestionsResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @ParameterizedTest
    @ValueSource(strings = ["ytm/search_suggestions/endpoint_response.json"])
    fun testParser( fileName: String ) {
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            inStream.decode<SearchSuggestionsResponseImpl>()
        }

        Assertions.assertNotNull( result )
    }
}