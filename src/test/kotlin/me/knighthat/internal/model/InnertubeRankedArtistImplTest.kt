package me.knighthat.internal.model

import me.knighthat.innertube.decode
import me.knighthat.innertube.model.InnertubeRankedArtist
import me.knighthat.internal.response.MusicResponsiveListItemRendererImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class InnertubeRankedArtistImplTest {

    companion object {

        @JvmStatic
        fun musicResponsiveListItemRendererProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "ytm/browse/charts_artist_ranking_musicResponsiveListItemRenderer1.json",
                "UCR28YDxjDE3ogQROaNdnRbQ",
                "YoungBoy Never Broke Again",
                2,
                "14.5M subscribers",
                "1",
                "ARROW_DROP_UP"
            ),
            Arguments.of(
                "ytm/browse/charts_artist_ranking_musicResponsiveListItemRenderer2.json",
                "UC5xaQ6_dP7EGDmGLzVGZ1Ow",
                "Morgan Wallen",
                2,
                "3.62M subscribers",
                "2",
                "ARROW_DROP_DOWN"
            ),
            Arguments.of(
                "ytm/browse/charts_artist_ranking_musicResponsiveListItemRenderer3.json",
                "UC0kxNxFQCK6d2spPz5Sme7Q",
                "Fuerza Regida",
                2,
                "5.52M subscribers",
                "3",
                "ARROW_CHART_NEUTRAL"
            )
        )
    }

    fun assertProperties(
        artist: InnertubeRankedArtist,
        id: String,
        name: String,
        thumbnailCount: Int,
        shortNumSubscribers: String?,
        rank: String,
        iconType: String
    ) {
        assertEquals( id, artist.id )
        assertEquals( name, artist.name )
        assertEquals( thumbnailCount, artist.thumbnails.size )

        assertNull( artist.description )
        assertNull( artist.longNumSubscribers )

        if( shortNumSubscribers != null ) {
            assertNotNull( artist.shortNumSubscribers )
            assertEquals( shortNumSubscribers, artist.shortNumSubscribers )
        } else
            assertNull( artist.shortNumSubscribers )

        assertNull( artist.shortNumMonthlyAudience )
        assertTrue( artist.sections.isEmpty() )
        assertEquals( rank, artist.rank )
        assertEquals( iconType, artist.iconType )
    }

    @ParameterizedTest
    @MethodSource("musicResponsiveListItemRendererProvider")
    fun testFromMusicResponsiveListItemRenderer(
        fileName: String,
        id: String,
        name: String,
        thumbnailCount: Int,
        shortNumSubscribers: String?,
        rank: String,
        iconType: String
    ) {
        ClassLoader.getSystemResourceAsStream( fileName )
            .also( ::assertNotNull )
            ?.use { inStream ->
                val renderer = inStream.decode<MusicResponsiveListItemRendererImpl>()
                assertNotNull( renderer )

                assertProperties(
                    InnertubeRankedArtistImpl.from( renderer ),
                    id,
                    name,
                    thumbnailCount,
                    shortNumSubscribers,
                    rank,
                    iconType
                )
            }
    }
}