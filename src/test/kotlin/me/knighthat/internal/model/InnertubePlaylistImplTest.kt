package me.knighthat.internal.model

import me.knighthat.innertube.Constants
import me.knighthat.innertube.decode
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.internal.response.BrowseResponseImpl
import me.knighthat.internal.response.MusicTwoRowItemRendererImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class InnertubePlaylistImplTest {

    companion object {

        @JvmStatic
        fun twoColumnBrowseResultsRendererProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "ytm/browse/playlist_twoColumnBrowseResultsRenderer2.json",
                "VLRDCLAK5uy_mPolD_J22gS1SKxufARWcTZd1UrAH_0ZI",
                "deep chill",
                4,
                "Your eternal home for deep house, chill electronic, ambient",
                "156 songs • 7+ hours",
                1,
                100,
                "4qmFsgKNARItVkxSRENMQUs1dXlfbVBvbERfSjIyZ1MxU0t4dWZBUldjVFpkMVVyQUhfMFpJGi5laFZRVkRwRlozUk1aREJvYmxsclVuSk5SMnQ0VlZHU0FRTUl1Z1R3QVFBJTNEmgIrUkRDTEFLNXV5X21Qb2xEX0oyMmdTMVNLeHVmQVJXY1RaZDFVckFIXzBaSQ%3D%3D",
            ),
            Arguments.of(
                "ytm/browse/playlist_twoColumnBrowseResultsRenderer3.json",
                "VLPLtILsETdHuj5rynRpGqVfPuiOs9mCZxn4",
                "2010-late2016 ish Anime OP/ED",
                3,
                null,
                "448 views • 74 tracks • 5 hours, 6 minutes",
                1,
                74,
                null
            ),
            Arguments.of(
                "ytm/browse/playlist_twoColumnBrowseResultsRenderer1.json",
                "VLRDCLAK5uy_nZiG9ehz_MQoWQxY5yElsLHCcG0tv9PRg",
                "Classic Rock's Greatest Hits",
                4,
                "Essential hits from the classic rock era, featuring long hair, big solos and singers who graduated from frontman school with honors!",
                "119 songs • 7+ hours",
                1,
                100,
                "4qmFsgKNARItVkxSRENMQUs1dXlfblppRzllaHpfTVFvV1F4WTV5RWxzTEhDY0cwdHY5UFJnGi5laFZRVkRwRlozUnZUVVJTUkZORWJGcFhiVTUzVTFHU0FRTUl1Z1R3QVFBJTNEmgIrUkRDTEFLNXV5X25aaUc5ZWh6X01Rb1dReFk1eUVsc0xIQ2NHMHR2OVBSZw%3D%3D"
            ),
            Arguments.of(
                "ytm/browse/playlist_twoColumnBrowseResultsRenderer4.json",
                "VLPLOpIT4aZz_neVC-cWJiwkPoA2aogjMHoK",
                "good songs",
                3,
                null,
                "64 views • 186 tracks • 4+ hours",
                1,
                100,
                "4qmFsgKHARIkVkxQTE9wSVQ0YVp6X25lVkMtY1dKaXdrUG9BMmFvZ2pNSG9LGjplaDVRVkRwRFIxRnBSVVZKZUZGNlVUTk9hbXN6VGpCU1JFOVZXa2ROUkVXU0FRTUl1Z1R3QVFFJTNEmgIiUExPcElUNGFael9uZVZDLWNXSml3a1BvQTJhb2dqTUhvSw%3D%3D"
            )
        )
    }

    private fun assertProperties(
        playlist: InnertubePlaylist,
        id: String,
        name: String,
        thumbnailCount: Int,
        description: String?,
        subtitle: String?,
        continuationCount: Int,
        songCount: Int,
        songContinuation: String?
    ) {
        assertEquals( id, playlist.id )
        assertEquals( name, playlist.name )
        assertEquals( thumbnailCount, playlist.thumbnails.size )

        if( description != null ) {
            assertNotNull( playlist.description )
            assertEquals( description, playlist.description )
        } else
            assertNull( playlist.description )

        if( subtitle != null ) {
            assertNotNull( playlist.subtitle )
            assertNotNull( playlist.subtitleText )
            assertEquals( subtitle, playlist.subtitleText )
        } else {
            assertNull( playlist.subtitle )
            assertNull( playlist.subtitleText )
        }

        assertEquals( continuationCount, playlist.continuations.size )
        assertEquals( songCount, playlist.songs.size )

        if( songContinuation != null ) {
            assertNotNull( playlist.songContinuation )
            assertEquals( songContinuation, playlist.songContinuation )
        } else
            assertNull( playlist.songContinuation )
    }

    @ParameterizedTest
    @MethodSource("twoColumnBrowseResultsRendererProvider")
    fun testFromTwoColumnBrowseResultsRenderer(
        fileName: String,
        id: String,
        name: String,
        thumbnailCount: Int,
        description: String?,
        subtitle: String?,
        continuationCount: Int,
        songCount: Int,
        songContinuation: String?
    ) {
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<BrowseResponseImpl.ContentsImpl.TwoColumnBrowseResultsRendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubePlaylistImpl.from( Constants.CHROME_WINDOWS_VISITOR_DATA, renderer ),
                           id,
                           name,
                           thumbnailCount,
                           description,
                           subtitle,
                           continuationCount,
                           songCount,
                           songContinuation
                       )
                   }
    }

    @Test
    fun testFromMusicTwoRowItemRenderer() {
        val fileName = "ytm/browse/artist_playlist_musicTwoRowItemRenderer.json"
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val renderer = inStream.decode<MusicTwoRowItemRendererImpl>()
                       assertNotNull( renderer )

                       assertProperties(
                           InnertubePlaylistImpl.from( renderer ),
                           "VLRDCLAK5uy_llO_Q2poD5rxl31e6b5DClFPaM53sm8fI",
                           "Presenting Kendrick Lamar",
                           2,
                           null,
                           null,
                           0,
                           0,
                           null
                      )
                    }
    }
}
