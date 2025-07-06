package me.knighthat.internal.model

import me.knighthat.innertube.decode
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.internal.response.MusicResponsiveListItemRendererImpl
import me.knighthat.internal.response.PlaylistPanelRendererImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


class InnertubeSongImplTest {

    companion object {

        @JvmStatic
        fun musicResponsiveListItemRendererProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "ytm/browse/artist_song_musicResponsiveListItemRenderer.json",
                "phLb_SoPBlA",
                "Not Like Us",
                2,
                true,
                null,
                true,
                1,
                "Kendrick Lamar"
            ),
            Arguments.of(
                "ytm/browse/album_song_musicResponsiveListItemRenderer.json",
                "KEG7b851Ric",
                "Taste",
                0,
                false,
                "2:38",
                false,
                0,
                ""
            ),
            Arguments.of(
                "ytm/browse/playlist_song_musicResponsiveListItemRenderer_1.json",
                "jvipPYFebWc",
                "Roundtable Rival",
                1,
                false,
                "3:39",
                false,
                1,
                "Lindsey Stirling"
            ),
            Arguments.of(
                "ytm/browse/playlist_song_musicResponsiveListItemRenderer_2.json",
                "5p_CJiP0KpY",
                "You Shook Me All Night Long (Live) (duet with Anastacia-Live from VH1 Divas)",
                2,
                false,
                "3:52",
                false,
                1,
                "Céline Dion & Anastacia"
            ),
            Arguments.of(
                "ytm/browse/playlist_continued_song_musicResponsiveListItemRenderer.json",
                "Qzb2VcBuA3U",
                "Clint Eastwood",
                2,
                true,
                "5:41",
                true,
                1,
                "Gorillaz & Del The Funky Homosapien"
            ),
            Arguments.of(
                "ytm/search/song_musicResponsiveListItemRenderer.json",
                "w4nihuYVTW0",
                "What is Love?",
                2,
                false,
                "3:29",
                true,
                1,
                "TWICE"
            )
        )
    }

    private fun assertProperties(
        song: InnertubeSong,
        id: String,
        name: String,
        thumbnailCount: Int,
        isExplicit: Boolean,
        durationText: String?,
        isAlbumExist: Boolean,
        artistCount: Int,
        artistsText: String
    ) {
        assertEquals( id, song.id )
        assertEquals( name, song.name )
        assertEquals( thumbnailCount, song.thumbnails.size )
        assertEquals( isExplicit, song.isExplicit )

        if( durationText != null ) {
            assertNotNull( song.durationText )
            assertEquals( durationText, song.durationText )
        } else
            assertNull( song.durationText )

        if( isAlbumExist )
            assertNotNull( song.album )
        else
            assertNull( song.album )

        assertEquals( artistCount, song.artists.size )
        assertEquals( artistsText, song.artistsText )
    }

    @ParameterizedTest
    @MethodSource("musicResponsiveListItemRendererProvider")
    fun testFromMusicResponsiveListItemRenderer(
        fileName: String,
        id: String,
        name: String,
        thumbnailCount: Int,
        isExplicit: Boolean,
        durationText: String?,
        isAlbumExist: Boolean,
        artistCount: Int,
        artistsText: String
    ) {
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inputStream ->
                       val renderer = inputStream.decode<MusicResponsiveListItemRendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubeSongImpl.from( renderer ),
                           id,
                           name,
                           thumbnailCount,
                           isExplicit,
                           durationText,
                           isAlbumExist,
                           artistCount,
                           artistsText
                       )
                   }
    }

    @Test
    fun testFromPlaylistPanelRendererContentVideoRenderer() {
        val fileName = "ytm/next/song_playlistPanelVideoRenderer.json"
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inputStream ->
                       val renderer = inputStream.decode<PlaylistPanelRendererImpl.ContentImpl.VideoRendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubeSongImpl.from( renderer ),
                           "a_H0K9W984E",
                           "TU SANCHO",
                           6,
                           true,
                           "2:58",
                           true,
                           1,
                           "Fuerza Regida"
                       )
                   }
    }
}