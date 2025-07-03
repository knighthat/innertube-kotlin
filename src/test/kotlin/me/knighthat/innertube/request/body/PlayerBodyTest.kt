package me.knighthat.innertube.request.body

import me.knighthat.innertube.JSON
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlayerBodyTest {

    @Test
    fun testInvalidBuilder() {
        assertThrowsExactly(AssertionError::class.java ) {
            PlayerBody.builder( Context.WEB_REMIX_DEFAULT ).build()
        }
    }

    @Test
    fun testDefaultValuesSerialization() {
        val body: PlayerBody = PlayerBody.builder( Context.WEB_REMIX_DEFAULT )
                                         .videoId( "1JVzMwv3YpQ" )
                                         .build()
        assertEquals( "1JVzMwv3YpQ", body.videoId )
        assertNull( body.params )
        assertNotNull( body.racyCheckOk )
        assertTrue { body.racyCheckOk!! }
        assertNotNull( body.contentCheckOk )
        assertTrue { body.contentCheckOk!! }
        assertNull( body.serviceIntegrityDimensions )

        val jsonStr = JSON.encodeToString( body )
        val decodedBody: PlayerBody = JSON.decodeFromString<PlayerBody>( jsonStr )
        assertEquals( body, decodedBody )
    }

    @Test
    fun testDefinedValuesSerialization() {
        val body: PlayerBody = PlayerBody.builder( Context.WEB_REMIX_DEFAULT )
                                         .videoId( "1JVzMwv3YpQ" )
                                         .contentCheckOk( false )
                                         .racyCheckOk( false )
                                         .poToken( "1234567890ab" )
                                         .params( "wAEB" )
                                         .build()
        assertEquals( "1JVzMwv3YpQ", body.videoId )

        assertNotNull( body.params )
        assertEquals( "wAEB", body.params )

        assertNotNull( body.racyCheckOk )
        assertFalse { body.racyCheckOk!! }

        assertNotNull( body.contentCheckOk )
        assertFalse { body.contentCheckOk!! }

        assertNotNull( body.serviceIntegrityDimensions )
        assertEquals( "1234567890ab", body.serviceIntegrityDimensions!!.poToken )

        val jsonStr = JSON.encodeToString( body )
        val decodedBody: PlayerBody = JSON.decodeFromString<PlayerBody>( jsonStr )
        assertEquals( body, decodedBody )
    }
}