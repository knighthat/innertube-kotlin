package me.knighthat.innertube.request.body

import me.knighthat.innertube.JSON
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class NextBodyTest {

    @Test
    fun testInvalidBuilder() {
        assertThrowsExactly(AssertionError::class.java ) {
            NextBody.builder( Context.IOS_DEFAULT ).build()
        }
    }

    @Test
    fun testDefaultValuesSerialization() {
        val body: NextBody = NextBody.builder( Context.IOS_DEFAULT )
                                     .videoId( "QH0zo6AcP6g" )
                                     .build()
        assertNotNull( body.videoId )
        assertEquals( "QH0zo6AcP6g", body.videoId )
        assertNull( body.playlistId )
        assertNull( body.params )

        val jsonStr = JSON.encodeToString( body )
        val decodedBody = JSON.decodeFromString<NextBody>( jsonStr )
        assertEquals( body, decodedBody )
    }

    @Test
    fun testDefinedValuesSerialization() {
        val body: NextBody = NextBody.builder( Context.IOS_DEFAULT )
                                     .videoId( "QH0zo6AcP6g" )
                                     .playlistId( "RDAMVMQH0zo6AcP6g" )
                                     .params( "wAEB" )
                                     .build()
        assertNotNull( body.videoId )
        assertEquals( "QH0zo6AcP6g", body.videoId )

        assertNotNull( body.playlistId )
        assertEquals( "RDAMVMQH0zo6AcP6g", body.playlistId )

        assertNotNull( body.params )
        assertEquals( "wAEB", body.params )

        val jsonStr = JSON.encodeToString( body )
        val decodedBody = JSON.decodeFromString<NextBody>( jsonStr )
        assertEquals( body, decodedBody )
    }
}