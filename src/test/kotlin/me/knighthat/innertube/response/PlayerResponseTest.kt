package me.knighthat.innertube.response

import me.knighthat.innertube.decode
import me.knighthat.internal.response.PlayerResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PlayerResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @ParameterizedTest
    @ValueSource(strings = ["ytm/player/endpoint_response1.json", "ytm/player/endpoint_response2.json"])
    fun testParser( fileName: String ) {
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            inStream.decode<PlayerResponseImpl>()
        }

        Assertions.assertNotNull( result )
    }
}