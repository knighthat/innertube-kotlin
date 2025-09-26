package me.knighthat.innertube.response

import me.knighthat.innertube.decode
import me.knighthat.internal.response.NextResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class NextResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @ParameterizedTest
    @ValueSource(strings = [
        "ytm/next/endpoint_response.json",
        "ytm/next/endpoint_video_response.json",
        "yt/next/endpoint_response.json"
    ])
    fun testParser( fileName: String ) {
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            inStream.decode<NextResponseImpl>()
        }

        Assertions.assertNotNull( result )
    }
}