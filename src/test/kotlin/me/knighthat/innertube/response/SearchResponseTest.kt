package me.knighthat.innertube.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import me.knighthat.internal.response.SearchResponseImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SearchResponseTest {

    companion object {
        val JSON: Json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testParser() {
        val fileName = "ytm/search/endpoint_response.json"
        val result = ClassLoader.getSystemResourceAsStream(fileName)?.use { inStream ->
            JSON.decodeFromStream<SearchResponseImpl>(inStream)
        }

        Assertions.assertNotNull(result)
        Assertions.assertEquals(
            "CgtGc1d2aUdLVjZiVSjYrqTCBjIKCgJVUxIEGgAgHA%3D%3D",
            result!!.responseContext.visitorData
        )
        Assertions.assertTrue(result.contents.tabbedSearchResultsRenderer.tabs::isNotEmpty)
    }

    @Test
    fun test() {
        Assertions.assertFalse { false }
    }
}