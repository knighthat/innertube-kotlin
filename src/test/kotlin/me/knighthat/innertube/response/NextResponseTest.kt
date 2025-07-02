package me.knighthat.innertube.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import me.knighthat.innertube.response.JsonParser.JSON
import me.knighthat.internal.response.NextResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class NextResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @Test
    fun testParser() {
        val fileName = "ytm/next/endpoint_response.json"
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            JSON.decodeFromStream<NextResponseImpl>( inStream )
        }

        Assertions.assertNotNull( result )
        Assertions.assertEquals(
            "CgtEa3I0OVk5S0xxVSie8JLDBjIKCgJVUxIEGgAgKA%3D%3D",
            result!!.responseContext.visitorData
        )

        val renderer = result.contents.singleColumnMusicWatchNextResultsRenderer
        Assertions.assertNotNull( renderer )
        Assertions.assertEquals( 3, renderer!!.tabbedRenderer.watchNextTabbedResultsRenderer.tabs.size )
    }
}