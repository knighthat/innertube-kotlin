package me.knighthat.innertube.response

import me.knighthat.innertube.decode
import me.knighthat.internal.response.SearchResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SearchResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @ParameterizedTest
    @ValueSource(strings = ["ytm/search/endpoint_response.json"])
    fun testParser( fileName: String ) {
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            inStream.decode<SearchResponseImpl>()
        }

        Assertions.assertNotNull( result )
    }
}