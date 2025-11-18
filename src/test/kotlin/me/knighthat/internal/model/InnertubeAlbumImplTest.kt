package me.knighthat.internal.model

import me.knighthat.innertube.Innertube
import me.knighthat.innertube.InnertubeProvider
import me.knighthat.innertube.decode
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.response.InnertubeImplTest
import me.knighthat.internal.InnertubeImpl
import me.knighthat.internal.response.BrowseResponseImpl
import me.knighthat.internal.response.MusicResponsiveListItemRendererImpl
import me.knighthat.internal.response.MusicTwoRowItemRendererImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class InnertubeAlbumImplTest {

    companion object {

        @BeforeAll
        @JvmStatic
        fun setup() = Innertube.setProvider( InnertubeProvider() )

        @JvmStatic
        fun musicTwoRowItemRendererProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "ytm/browse/music_home_album_musicTwoRowItemRenderer.json",
                "MPREb_T0ssT2HczFT",
                "Short n' Sweet (Deluxe)",
                2,
                true,
                1,
                ""
            ),
            Arguments.of(
                "ytm/browse/artist_album_musicTwoRowItemRenderer.json",
                "MPREb_wCND3crJyDP",
                "GNX",
                2,
                true,
                0,
                "2024"
            ),
            Arguments.of(
                "ytm/browse/album_alternative_musicTwoRowItemRenderer.json",
                "MPREb_m10OjDmOhfv",
                "Hybrid Theory (Bonus Edition)",
                2,
                false,
                1,
                ""
            )
        )

        @JvmStatic
        fun browseResponseProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "ytm/browse/album_browseResponse1.json",
                "MPREb_FWIMEPTHFsY",
                "Starboy",
                4,
                true,
                1,
                "2016",
                "https://music.youtube.com/playlist?list=OLAK5uy_nl-XrT4Q4vuhtuzHGfIuY5a0Rao4J8LdM",
                """
                    Starboy is the third studio album by the Canadian singer-songwriter the Weeknd, released on November 25, 2016, through XO and Republic Records. It features guest appearances from Daft Punk, Lana Del Rey, Kendrick Lamar, and Future. As the album's executive producers, the Weeknd and Doc McKinney enlisted a variety of producers such as Diplo, Cashmere Cat, Metro Boomin, Frank Dukes, and Labrinth, among others.
                    Starboy was supported by eight singles, including the US Billboard Hot 100 number-ones "Starboy" and "Die for You", and the top-five single "I Feel It Coming". It received generally favorable reviews from critics and debuted at number one on the US Billboard 200 with 348,000 album-equivalent units, becoming the Weeknd's second consecutive number-one album. It also debuted at number one on Billboard's Canadian Albums Chart. Starboy won Best Urban Contemporary Album at the 60th Annual Grammy Awards in 2018, marking the Weeknd's second win in that category. As of April 2022, the album is certified four-times platinum by the Recording Industry Association of America.

                    From Wikipedia (https://en.wikipedia.org/wiki/Starboy...) under Creative Commons Attribution CC-BY-SA 3.0 (https://creativecommons.org/licenses/...)
                """.trimIndent(),
                "18 songs • 1 hour, 8 minutes",
                18,
                2
            ),
            Arguments.of(
                "ytm/browse/album_browseResponse2.json",
                "MPREb_5HsqbBKw813",
                "25",
                4,
                false,
                1,
                "2015",
                "https://music.youtube.com/playlist?list=OLAK5uy_mV4Nb1vqzEvRAi-8yz-9Bd0YT5bkmoojI",
                """
                    25 is the third studio album by the English singer-songwriter Adele, released on 20 November 2015 by XL Recordings and Columbia Records. The album is titled as a reflection of her life and frame of mind at 25 years old and is termed a "make-up record". Its lyrical content features themes of Adele "yearning for her old self, her nostalgia", and "melancholia about the passage of time" according to an interview with the singer by Rolling Stone, as well as themes of motherhood, new love, and regret.
                    In contrast to Adele's previous works, the production of 25 incorporated the use of electronic elements and creative rhythmic patterns, with elements of 1980s R&B and organs. Four singles were released to promote the album, with "Hello" becoming an international number one song and the fastest selling digital single in the US, with over a million copies sold within a week of its release, "Send My Love" charted within the top 10 and "When We Were Young" and "Water Under the Bridge" charted within the top 20 across Europe and North America.

                    From Wikipedia (https://en.wikipedia.org/wiki/25_(Ade...) under Creative Commons Attribution CC-BY-SA 3.0 (https://creativecommons.org/licenses/...)
                """.trimIndent(),
                "11 songs • 48 minutes",
                11,
                1
            )
        )
    }

    private fun assertProperties(
        album: InnertubeAlbum,
        id: String,
        name: String,
        thumbnailCount: Int,
        isExplicit: Boolean,
        artistCount: Int,
        year: String,
        urlCanonical: String?,
        description: String?,
        subtitle: String?,
        songCount: Int,
        sectionCount: Int
    ) {
        assertEquals( id, album.id )
        assertEquals( name, album.name )
        assertEquals( thumbnailCount, album.thumbnails.size )
        assertEquals( isExplicit, album.isExplicit )
        assertEquals( artistCount, album.artists.size )
        assertEquals( year, album.year )

        if( urlCanonical != null ) {
            assertNotNull( album.urlCanonical )
            assertEquals( urlCanonical, album.urlCanonical )
        } else
            assertNull( album.urlCanonical )

        if( description != null ) {
            assertNotNull( album.description )
            assertEquals( description, album.description )
        } else
            assertNull( album.description )

        if( subtitle != null ) {
            assertNotNull( album.subtitle )
            assertEquals( subtitle, album.subtitle )
        } else
            assertNull( album.subtitle )

        assertEquals( songCount, album.songs.size )
        assertEquals( sectionCount, album.sections.size )
    }

    @ParameterizedTest
    @MethodSource("musicTwoRowItemRendererProvider")
    fun testFromMusicTwoRowItemRenderer(
        fileName: String,
        id: String,
        name: String,
        thumbnailCount: Int,
        isExplicit: Boolean,
        artistCount: Int,
        year: String
    ) {
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<MusicTwoRowItemRendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubeAlbumImpl.from( renderer ),
                           id,
                           name,
                           thumbnailCount,
                           isExplicit,
                           artistCount,
                           year,
                           null,
                           null,
                           null,
                           0,
                           0
                       )
                   }
    }

    @Test
    fun testFromMusicResponsiveListItemRenderer() {
        val fileName = "ytm/search/album_musicResponsiveListItemRenderer.json"
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<MusicResponsiveListItemRendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubeAlbumImpl.from( renderer ),
                           "MPREb_LiSIhY0SLZZ",
                           "A Bar Song",
                           4,
                           false,
                           3,
                           "2025",
                           null,
                           null,
                           null,
                           0,
                           0
                       )
                   }
    }

    @ParameterizedTest
    @MethodSource("browseResponseProvider")
    suspend fun testFromBrowseResponse(
        fileName: String,
        id: String,
        name: String,
        thumbnailCount: Int,
        isExplicit: Boolean,
        artistCount: Int,
        year: String,
        urlCanonical: String?,
        description: String?,
        subtitle: String?,
        songCount: Int,
        sectionCount: Int
    ) {
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<BrowseResponseImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubeAlbumImpl.from( id, Localization.EN_US, renderer ),
                           id,
                           name,
                           thumbnailCount,
                           isExplicit,
                           artistCount,
                           year,
                           urlCanonical,
                           description,
                           subtitle,
                           songCount,
                           sectionCount
                       )
                   }
    }
}