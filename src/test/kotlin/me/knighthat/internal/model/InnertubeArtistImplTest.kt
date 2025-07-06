package me.knighthat.internal.model

import me.knighthat.innertube.decode
import me.knighthat.internal.response.BrowseResponseImpl
import me.knighthat.internal.response.MusicTwoRowItemRendererImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

class InnertubeArtistImplTest {

    @Test
    fun testFromMusicTwoRowItemRenderer() {
        val fileName = "ytm/browse/artist_related_artists_musicTwoRowItemRenderer.json"
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<MusicTwoRowItemRendererImpl>()
                       assertNotNull( renderer )

                       val artist = InnertubeArtistImpl.from( renderer )
                       assertEquals( "UCRY5dYsbIN5TylSbd7gVnZg", artist.id )
                       assertEquals( "Kanye West", artist.name )
                       assertEquals( 2, artist.thumbnails.size )
                       assertNull( artist.description )
                       assertNull( artist.longNumSubscribers )
                       assertNull( artist.shortNumSubscribers )
                       assertNotNull( artist.shortNumMonthlyAudience )
                       assertEquals( "144M monthly audience", artist.shortNumMonthlyAudience )
                       assertTrue( artist.sections.isEmpty() )
                   }
    }

    @Test
    fun testFromBrowseResponse() {
        val fileName = "ytm/browse/artist_browseResponse.json"
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<BrowseResponseImpl>()
                       assertNotNull( renderer )

                       val artist = InnertubeArtistImpl.from( renderer )
                       assertEquals( "UC3lBXcrKFnFAFkfVk5WuKcQ", artist.id)
                       assertEquals( "Kendrick Lamar", artist.name)

                       assertEquals( 5, artist.thumbnails.size )

                       assertNotNull( artist.description )
                       // Due to multiple new lines, triple quotes must be used to preserve all of them
                       val expectedDescription = """
                           Kendrick Lamar Duckworth is an American rapper, songwriter and record producer. Regarded as one of the greatest rappers of all time, he was awarded the 2018 Pulitzer Prize for Music, becoming the first musician outside of the classical and jazz genres to receive the award.
                           Born in Compton, California, Lamar began releasing music under the stage name K.Dot while attending high school. He signed with Top Dawg Entertainment in 2005 and co-founded the hip hop supergroup Black Hippy there. Following the 2011 release of his alternative rap debut album Section.80, Lamar secured a joint contract with Dr. Dre's Aftermath Entertainment and Interscope Records. He rose to prominence with his gangsta rap-influenced second album Good Kid, M.A.A.D City, which became the longest-charting hip hop studio album in Billboard 200 history; Rolling Stone named it the greatest concept album of all time.
    
                           From Wikipedia (https://en.wikipedia.org/wiki/Kendrick_Lamar) under Creative Commons Attribution CC-BY-SA 3.0 (http://creativecommons.org/licenses/by-sa/3.0/legalcode)
                       """.trimIndent()
                       assertEquals( expectedDescription, artist.description!! )

                       assertNotNull( artist.longNumSubscribers )
                       assertEquals( "19.1M subscribers", artist.longNumSubscribers )

                       assertNotNull( artist.shortNumSubscribers )
                       assertEquals( "19.1M", artist.shortNumSubscribers )

                       assertNotNull( artist.shortNumMonthlyAudience )
                       assertEquals( "434M monthly audience", artist.shortNumMonthlyAudience )

                       artist.sections.forEach {
                           println(it.title)
                       }
                       assertEquals(
                           7,      // musicDescriptionShelfRenderer is excluded
                           artist.sections
                                 .filter { it.contents.isNotEmpty() }
                                 .size
                       )
                   }
    }
}