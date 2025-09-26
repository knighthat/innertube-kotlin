package me.knighthat.internal.model

import me.knighthat.innertube.decode
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.internal.response.BrowseResponseImpl
import me.knighthat.internal.response.MusicTwoRowItemRendererImpl
import me.knighthat.internal.response.PrimaryResultsImpl.ResultsImpl.ContentImpl.VideoSecondaryInfoRendererImpl.OwnerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class InnertubeArtistImplTest {

    companion object {

        @JvmStatic
        fun browseResponseProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "ytm/browse/artist_browseResponse.json",
                "UCprAFmT0C6O4X0ToEXpeFTQ",
                "Kendrick Lamar",
                5,
                // Due to multiple new lines, triple quotes must be used to preserve all of them
                """
                   Kendrick Lamar Duckworth is an American rapper, songwriter and record producer. Regarded as one of the greatest rappers of all time, he was awarded the 2018 Pulitzer Prize for Music, becoming the first musician outside of the classical and jazz genres to receive the award.
                   Born in Compton, California, Lamar began releasing music under the stage name K.Dot while attending high school. He signed with Top Dawg Entertainment in 2005 and co-founded the hip hop supergroup Black Hippy there. Following the 2011 release of his alternative rap debut album Section.80, Lamar secured a joint contract with Dr. Dre's Aftermath Entertainment and Interscope Records. He rose to prominence with his gangsta rap-influenced second album Good Kid, M.A.A.D City, which became the longest-charting hip hop studio album in Billboard 200 history; Rolling Stone named it the greatest concept album of all time.
                
                   From Wikipedia (https://en.wikipedia.org/wiki/Kendrick_Lamar) under Creative Commons Attribution CC-BY-SA 3.0 (http://creativecommons.org/licenses/by-sa/3.0/legalcode)
                """.trimIndent(),
                "19.1M",
                "19.1M subscribers",
                "434M monthly audience",
                7
            ),
            Arguments.of(
                "ytm/browse/endpoint_artist_non_music_response.json",
                "UCtkSwwONNeMC3C2aHxGeFmA",
                "Inspira",
                5,
                null,
                null,
                null,
                null,
                2
            )
        )
    }

    private fun assertProperties(
        artist: InnertubeArtist,
        id: String,
        name: String,
        thumbnailCount: Int,
        description: String?,
        shortNumSubscribers: String?,
        longNumSubscribers: String?,
        shortNumMonthlyAudience: String?,
        sectionCount: Int
    ) {
        assertEquals( id, artist.id )
        assertEquals( name, artist.name )
        assertEquals( thumbnailCount, artist.thumbnails.size )

        if( description != null ) {
            assertNotNull( artist.description )
            assertEquals( description, artist.description )
        } else
            assertNull( artist.description )

        if( longNumSubscribers != null ) {
            assertNotNull( artist.longNumSubscribers )
            assertEquals( longNumSubscribers, artist.longNumSubscribers )
        } else
            assertNull( artist.longNumSubscribers )

        if( shortNumSubscribers != null ) {
            assertNotNull( artist.shortNumSubscribers )
            assertEquals( shortNumSubscribers, artist.shortNumSubscribers )
        } else
            assertNull( artist.shortNumSubscribers )

        if( shortNumMonthlyAudience != null ) {
            assertNotNull( artist.shortNumMonthlyAudience )
            assertEquals( shortNumMonthlyAudience, artist.shortNumMonthlyAudience )
        } else
            assertNull( artist.shortNumMonthlyAudience )

        assertEquals(
            sectionCount,
            artist.sections     // musicDescriptionShelfRenderer is excluded
                  .filter { it.contents.isNotEmpty() }
                  .size
        )
    }

    @Test
    fun testFromMusicTwoRowItemRenderer() {
        val fileName = "ytm/browse/artist_related_artists_musicTwoRowItemRenderer.json"
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<MusicTwoRowItemRendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubeArtistImpl.from( renderer ),
                           "UCRY5dYsbIN5TylSbd7gVnZg",
                           "Kanye West",
                           2,
                           null,
                           null,
                           null,
                           "144M monthly audience",
                           0
                       )
                   }
    }

    @ParameterizedTest
    @MethodSource("browseResponseProvider")
    fun testFromBrowseResponse(
        fileName: String,
        id: String,
        name: String,
        thumbnailCount: Int,
        description: String?,
        shortNumSubscribers: String?,
        longNumSubscribers: String?,
        shortNumMonthlyAudience: String?,
        sectionCount: Int
    ) {
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<BrowseResponseImpl>()
                       assertNotNull( renderer )

                       val expectedDescription =

                       assertProperties(
                           InnertubeArtistImpl.from( renderer ),
                           id,
                           name,
                           thumbnailCount,
                           description,
                           shortNumSubscribers,
                           longNumSubscribers,
                           shortNumMonthlyAudience,
                           sectionCount
                       )
                   }
    }

    @Test
    fun testFromVideoOwnerRenderer() {
        val fileName = "yt/next/artist_videoOwnerRenderer.json"
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<OwnerImpl.RendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubeArtistImpl.from( renderer ),
                           id = "UCzgxx_DM2Dcb9Y1spb9mUJA",
                           name = "TWICE",
                           thumbnailCount = 3,
                           description = null,
                           shortNumSubscribers = null,
                           longNumSubscribers = "18.2M subscribers",
                           shortNumMonthlyAudience = null,
                           sectionCount = 0
                       )
                   }
    }
}