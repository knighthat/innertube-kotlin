package me.knighthat.innertube.request.body

import me.knighthat.innertube.JSON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test

class SearchSuggestionsBodyTest {

    @Test
    fun testInvalidBuilder() {
        assertThrowsExactly( AssertionError::class.java ) {
            SearchSuggestionsBody.builder( Context.IOS_DEFAULT ).build()
        }
    }

    @Test
    fun testSerialization() {
        val body: SearchSuggestionsBody = SearchSuggestionsBody.builder( Context.IOS_DEFAULT )
                                                               .input( "take me to church" )
                                                               .build()
        assertEquals( "take me to church", body.input )

        val jsonStr = JSON.encodeToString( body )
        val decodedBody: SearchSuggestionsBody = JSON.decodeFromString<SearchSuggestionsBody>( jsonStr )
        assertEquals( body, decodedBody )
    }
}