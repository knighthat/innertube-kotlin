package me.knighthat.innertube.request.body

import me.knighthat.innertube.JSON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test

class SearchBodyTest {

    @Test
    fun testInvalidBuilder() {
        assertThrowsExactly(AssertionError::class.java ) {
            SearchBody.builder( Context.WEB_DEFAULT ).build()
        }
    }

    @Test
    fun testSerialization() {
        val body: SearchBody = SearchBody.builder( Context.WEB_DEFAULT )
                                         .query( "HOPE - nf" )
                                         .params( "wAEB" )
                                         .build()
        assertEquals( "HOPE - nf", body.query )
        assertEquals( "wAEB", body.params )

        val jsonStr = JSON.encodeToString( body )
        val decodedBody: SearchBody = JSON.decodeFromString<SearchBody>( jsonStr )
        assertEquals( body, decodedBody )
    }
}