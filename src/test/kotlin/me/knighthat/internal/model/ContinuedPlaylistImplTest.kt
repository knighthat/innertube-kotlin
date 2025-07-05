package me.knighthat.internal.model

import me.knighthat.innertube.decode
import me.knighthat.internal.response.MusicPlaylistShelfRendererImpl
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ContinuedPlaylistImplTest {

    companion object {

        @JvmStatic
        fun musicPlaylistShelfRendererContentProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    "ytm/browse/playlist_continued_musicPlaylistShelfRenderer_content_1.json",
                    "4qmFsgKFARIkVkxQTE1wMUJaRjg3T0twU3J1Z29aZElydmZsMFlBN2t2MUxnGjhlaDlRVkRwRFRXZENTV2hCZDAxcVJYbE5SVVpFVFZSbmVrNVZWWHBTVkdjMWtnRURDTG9FOEFFQZoCIlBMTXAxQlpGODdPS3BTcnVnb1pkSXJ2ZmwwWUE3a3YxTGc%3D",
                    100
                ),
                Arguments.of(
                    "ytm/browse/playlist_continued_musicPlaylistShelfRenderer_content_2.json",
                    "4qmFsgKFARIkVkxQTE1wMUJaRjg3T0twU3J1Z29aZElydmZsMFlBN2t2MUxnGjhlaDlRVkRwRFMzZERTV2hCZDFGVWFFUk9NRlpEVWxSVk1VNVVSVFZOZWswMWtnRURDTG9FOEFFQZoCIlBMTXAxQlpGODdPS3BTcnVnb1pkSXJ2ZmwwWUE3a3YxTGc%3D",
                    100
                )
            )
    }

    @ParameterizedTest
    @MethodSource("musicPlaylistShelfRendererContentProvider")
    fun testFromMusicPlaylistShelfRendererContentProvider(
        fileName: String,
        continuation: String?,
        songCount: Int
    ) {
        ClassLoader.getSystemResourceAsStream( fileName )
                   .also( ::assertNotNull )
                   ?.use { inStream ->
                       val contents = inStream.decode<List<MusicPlaylistShelfRendererImpl.ContentImpl>>()
                       assertNotNull( contents )

                       val continuedPlaylist = ContinuedPlaylistImpl.from( contents )
                       if(continuation != null) {
                           assertNotNull( continuedPlaylist.continuation )
                           assertEquals( continuation, continuedPlaylist.continuation )
                       } else
                           assertNull( continuedPlaylist.continuation )

                       assertEquals( songCount, continuedPlaylist.songs.size )
                   }
    }
}