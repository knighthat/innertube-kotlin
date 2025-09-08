package me.knighthat.innertube

import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeCharts
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.request.Request
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.response.PlayerResponse
import me.knighthat.innertube.response.Response
import me.knighthat.internal.InnertubeImpl
import me.knighthat.internal.model.AccountInfoImpl
import org.jetbrains.annotations.Blocking
import java.io.IOException

interface Innertube {

    companion object: Innertube by InnertubeImpl()

    fun setProvider( provider: Provider )

    fun browsePlaylist( playlistId: String, localization: Localization, useLogin: Boolean = true ): Result<InnertubePlaylist>

    fun browsePlaylistSongs( playlistId: String, localization: Localization ): Result<List<InnertubeSong>>

    /**
     * Request for more songs in a playlist.
     *
     *
     * `visitorData` and `continuation` are parameters
     * extracted from response when you browse for playlist.
     *
     * @param visitorData tracking parameter extracted from browse playlist response
     * @param continuation unique string of playlist to get next songs
     * @param params additional parameters (optional)
     */
    fun playlistContinued(
        visitorData: String?,
        continuation: String,
        localization: Localization,
        params: String? = null,
        useLogin: Boolean = false
    ): Result<ContinuedPlaylist>

    fun browseArtist( artistId: String, localization: Localization, params: String? = null ): Result<InnertubeArtist>

    fun browseAlbum( albumId: String, localization: Localization, params: String? = null ): Result<InnertubeAlbum>

    fun songBasicInfo( songId: String, localization: Localization, params: String? = null ): Result<InnertubeSong>

    fun radio(
        songId: String,
        localization: Localization,
        playlistId: String = "RDAMVM$songId",
        params: String? = null,
        includeProvidedSong: Boolean = false
    ): Result<List<InnertubeSong>>

    fun charts( localization: Localization, selectedValue: String, params: String? = null ): Result<InnertubeCharts>

    /**
     * **This call explicitly require login credentials.**
     */
    fun accountInfo( localization: Localization ): Result<AccountInfoImpl>

    /**
     * Get user's saved playlists.
     *
     * **This call explicitly require login credentials.**
     */
    fun library( localization: Localization ): Result<List<InnertubeItem>>

    fun player(
        songId: String,
        context: Context,
        localization: Localization,
        signatureTimestamp: Int?,
        visitorData: String?,
        useLogin: Boolean = false
    ): Result<PlayerResponse>

    interface Provider {

        val cookies: String
        val dataSyncId: String?
        val visitorData: String

        @Blocking
        @Throws(IOException::class)
        fun execute( request: Request ): Response
    }
}