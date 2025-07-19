package me.knighthat.innertube.response

import me.knighthat.innertube.decode
import me.knighthat.internal.response.BrowseResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BrowseResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @ParameterizedTest
    @ValueSource(strings = [
        "ytm/browse/endpoint_artist_response.json",
        "ytm/browse/endpoint_artist_non_music_response.json",
        "ytm/browse/endpoint_playlist_response.json",
        "ytm/browse/playlist_continued_browseResponse.json",
        "ytm/browse/endpoint_charts_response.json"
    ])
    fun testParser( fileName: String ) {
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            inStream.decode<BrowseResponseImpl>()
        }

        Assertions.assertNotNull( result )
    }
}