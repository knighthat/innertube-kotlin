package me.knighthat.innertube.request.body

import me.knighthat.innertube.JSON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

class BrowseBodyTest {

    @Test
    fun testBrowseIdSerialization() {
        val body: BrowseBody = BrowseBody.builder( Context.WEB_REMIX_DEFAULT )
                                         .browseId( "MPcdzoRuhNE" )
                                         .params( "wAEB" )
                                         .build()
        assertNull( body.continuation )
        assertEquals( "MPcdzoRuhNE", body.browseId )
        assertNotNull( body.params )
        assertEquals( "wAEB", body.params )

        val jsonStr = JSON.encodeToString( body )
        val decodedBody = JSON.decodeFromString<BrowseBody>( jsonStr )
        assertEquals( body, decodedBody )
    }

    @Test
    fun testContinuationSerialization() {
        val body: BrowseBody = BrowseBody.builder( Context.WEB_REMIX_DEFAULT )
                                         .continuation( "4qmFsgI0EiRWTFBMdElMc0VUZEh1ajVyeW5ScEdxVmZQdWlPczltQ1p4bjQaDGtnRURDTTBHOEFFQQ%3D%3D" )
                                         .params( "wAEB" )
                                         .build()
        assertNull( body.browseId )
        assertEquals(
            "4qmFsgI0EiRWTFBMdElMc0VUZEh1ajVyeW5ScEdxVmZQdWlPczltQ1p4bjQaDGtnRURDTTBHOEFFQQ%3D%3D",
            body.continuation
        )
        assertNotNull( body.params )
        assertEquals( "wAEB", body.params )

        val jsonStr: String = JSON.encodeToString( body )
        val decodedBody = JSON.decodeFromString<BrowseBody>( jsonStr )
        assertEquals( body, decodedBody )
    }

}