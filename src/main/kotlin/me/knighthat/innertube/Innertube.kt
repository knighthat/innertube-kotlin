package me.knighthat.innertube

import io.ktor.client.HttpClient
import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.HomePage
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeCharts
import me.knighthat.innertube.model.InnertubeContinuation
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.innertube.model.InnertubeSearch
import me.knighthat.innertube.model.InnertubeSearchSuggestion
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.model.InnertubeSongDetails
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.response.PlayerResponse
import me.knighthat.internal.InnertubeImpl
import me.knighthat.internal.model.AccountInfoImpl

interface Innertube {

    companion object: Innertube by InnertubeImpl()

    fun setProvider( provider: KtorProvider )

    suspend fun browsePlaylist( playlistId: String, localization: Localization, useLogin: Boolean = true ): Result<InnertubePlaylist>

    suspend fun browsePlaylistSongs( playlistId: String, localization: Localization ): Result<List<InnertubeSong>>

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
    suspend fun playlistContinued(
        visitorData: String?,
        continuation: String,
        localization: Localization,
        params: String? = null,
        useLogin: Boolean = false
    ): Result<ContinuedPlaylist>

    suspend fun browseArtist( artistId: String, localization: Localization, params: String? = null ): Result<InnertubeArtist>

    suspend fun browseAlbum( albumId: String, localization: Localization, params: String? = null ): Result<InnertubeAlbum>

    suspend fun songBasicInfo( songId: String, localization: Localization, params: String? = null ): Result<InnertubeSong>

    suspend fun songInfo( songId: String, localization: Localization ): Result<InnertubeSongDetails>

    suspend fun radio(
        songId: String,
        localization: Localization,
        playlistId: String = "RDAMVM$songId",
        params: String? = null,
        includeProvidedSong: Boolean = false
    ): Result<List<InnertubeSong>>

    suspend fun charts( localization: Localization, selectedValue: String, params: String? = null ): Result<InnertubeCharts>

    /**
     * **This call explicitly require login credentials.**
     */
    suspend fun accountInfo( localization: Localization ): Result<AccountInfoImpl>

    /**
     * Get user's saved playlists.
     *
     * **This call explicitly require login credentials.**
     */
    suspend fun library( localization: Localization ): Result<List<InnertubeItem>>

    suspend fun player(
        songId: String,
        context: Context,
        localization: Localization,
        signatureTimestamp: Int?,
        visitorData: String?,
        useLogin: Boolean = false
    ): Result<PlayerResponse>

    suspend fun homePage( localization: Localization ): Result<HomePage>

    suspend fun continuation(
        localization: Localization,
        visitorData: String?,
        continuation: String,
        params: String?
    ): Result<InnertubeContinuation>

    suspend fun searchSuggestion(
        localization: Localization,
        input: String
    ): Result<InnertubeSearchSuggestion>

    suspend fun search(
        localization: Localization,
        query: String,
        params: String?
    ): Result<InnertubeSearch>

    suspend fun searchContinuation(
        localization: Localization,
        visitorData: String?,
        continuation: String,
    ): Result<InnertubeContinuation>

    interface KtorProvider {

        val client: HttpClient
        val cookies: String
        val dataSyncId: String?
        val visitorData: String
    }
}