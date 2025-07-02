package me.knighthat.innertube.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import me.knighthat.innertube.response.JsonParser.JSON
import me.knighthat.internal.response.BrowseResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class BrowseResponseTest {

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @Test
    fun testParserArtist() {
        val fileName = "ytm/browse/endpoint_artist_response.json"
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            JSON.decodeFromStream<BrowseResponseImpl>( inStream )
        }

        Assertions.assertNotNull( result )
        Assertions.assertEquals(
            "CgswV2x0eGxKOXZobyjJmZjCBjIKCgJVUxIEGgAgKA%3D%3D",
            result!!.responseContext.visitorData
        )

        val renderer = result.contents.singleColumnBrowseResultsRenderer
        Assertions.assertNotNull( renderer )
        Assertions.assertEquals( 1, renderer!!.tabs.size )
        val sectionListRenderer = renderer.tabs.first().tabRenderer.content.sectionListRenderer
        Assertions.assertNotNull( sectionListRenderer )
        Assertions.assertEquals( 9, sectionListRenderer!!.contents.size )
    }

    /**
     * Simple test to ensure [me.knighthat.innertube.response.JsonParser.Companion.JSON] can successfully parse a json file
     */
    @Test
    fun testParserPlaylist() {
        val fileName = "ytm/browse/endpoint_playlist_response.json"
        val result = ClassLoader.getSystemResourceAsStream( fileName )?.use { inStream ->
            JSON.decodeFromStream<BrowseResponseImpl>( inStream )
        }

        Assertions.assertNotNull( result )
        Assertions.assertEquals(
            "CgtkZERlVm91RE1nRSiqs6LCBjIKCgJVUxIEGgAgWQ%3D%3D",
            result!!.responseContext.visitorData
        )

        val renderer = result.contents.twoColumnBrowseResultsRenderer
        Assertions.assertNotNull( renderer )
        Assertions.assertEquals( 1, renderer!!.tabs.size )
        val sectionListRenderer = renderer.tabs.first().tabRenderer.content.sectionListRenderer
        Assertions.assertNotNull( sectionListRenderer )
        Assertions.assertEquals( 1, sectionListRenderer!!.contents.size )

        val secondaryContents = renderer.secondaryContents
        Assertions.assertNotNull( secondaryContents )
        Assertions.assertEquals( 1, secondaryContents!!.sectionListRenderer.contents.size )
        val shelfRenderer = secondaryContents.sectionListRenderer.contents.first().musicPlaylistShelfRenderer
        Assertions.assertNotNull( shelfRenderer )
        Assertions.assertEquals(
            "RDCLAK5uy_nUdlc4NQ_V5C1F6c05AugV1IB7aB8xG-Q",
            shelfRenderer!!.playlistId
        )
    }
}