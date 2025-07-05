package me.knighthat.internal.model

import me.knighthat.innertube.decode
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.internal.response.MusicResponsiveListItemRendererImpl
import me.knighthat.internal.response.MusicTwoRowItemRendererImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class InnertubeAlbumImplTest {

    companion object {

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
    }

    private fun assertProperties(
        album: InnertubeAlbum,
        id: String,
        name: String,
        thumbnailCount: Int,
        isExplicit: Boolean,
        artistCount: Int,
        year: String
    ) {
        assertEquals( id, album.id )
        assertEquals( name, album.name )
        assertEquals( thumbnailCount, album.thumbnails.size )
        assertEquals( isExplicit, album.isExplicit )
        assertEquals( artistCount, album.artists.size )
        assertEquals( year, album.year )
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
                           year
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
                           "2025"
                       )
                   }
    }
}