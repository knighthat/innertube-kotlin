package me.knighthat.innertube.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import me.knighthat.innertube.response.JsonParser.JSON
import me.knighthat.internal.response.PlayerResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class PlayerResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @Test
    fun testParser() {
        val fileName = "ytm/player/endpoint_response.json"
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            JSON.decodeFromStream<PlayerResponseImpl>( inStream )
        }

        Assertions.assertNotNull( result )
        Assertions.assertEquals(
            "CgtSaDVqZHJsbDdLayiq8JLDBjIKCgJVUxIEGgAgZw%3D%3D",
            result!!.responseContext.visitorData
        )

        Assertions.assertEquals( "OK", result.playabilityStatus.status )

        val streamingData = result.streamingData
        Assertions.assertNotNull( streamingData )
        Assertions.assertEquals( 16, streamingData!!.adaptiveFormats.size )
    }
}