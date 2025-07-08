package me.knighthat.innertube

import kotlinx.serialization.json.Json
import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.request.Request
import me.knighthat.innertube.request.body.BrowseBody
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.request.body.RequestBody
import me.knighthat.innertube.response.Response
import me.knighthat.internal.model.ContinuedPlaylistImpl
import me.knighthat.internal.model.InnertubeArtistImpl
import me.knighthat.internal.model.InnertubePlaylistImpl
import me.knighthat.internal.response.BrowseResponseImpl
import org.intellij.lang.annotations.MagicConstant
import org.jetbrains.annotations.Blocking
import org.jetbrains.annotations.VisibleForTesting
import java.io.IOException


object Innertube {

    private val JSON: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    var client: Provider? = null

    @VisibleForTesting
    @Blocking
    @Throws(IOException::class)
    fun sendRequest(
        @MagicConstant(valuesFromClass = Request::class) method: String,
        host: String,
        @MagicConstant(valuesFromClass = Endpoints::class) endpoint: String,
        requestBody: RequestBody,
        headers: Map<String, List<String>>
    ): Response = client!!.execute(
        Request(method, headers, "$host/$endpoint", requestBody)
    )


    // END: Static fields/functions
    @Throws(IOException::class)
    private fun ytmBrowse( browseBody: BrowseBody, headers: Map<String, List<String>> ): Response =
        sendRequest(Request.POST, Constants.YOUTUBE_MUSIC_URL, Endpoints.BROWSE, browseBody, headers )

    fun browsePlaylist(
        playlistId: String,
        localization: Localization
    ): Result<InnertubePlaylist> {
        val context = Context(
            Context.WEB_REMIX_DEFAULT.client.copy(
                hl = localization.languageCode,
                gl = localization.regionCode
            )
        )
        val browseBody = BrowseBody.builder( context ).browseId( playlistId ).build()

        return runCatching {
            val response = ytmBrowse( browseBody, Constants.JSON_HEADERS )
            val browseResponse = JSON.decodeFromString<BrowseResponseImpl>( response.responseBody )

            InnertubePlaylistImpl.from(
                browseResponse.responseContext.visitorData!!,
                browseResponse.contents!!.twoColumnBrowseResultsRenderer!!
            )
        }
    }

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
        visitorData: String,
        continuation: String,
        localization: Localization,
        params: String?
    ): Result<ContinuedPlaylist> {
        val context = Context(
            Context.WEB_REMIX_DEFAULT.client.copy(
                hl = localization.languageCode,
                gl = localization.regionCode,
                visitorData = visitorData
            )
        )
        val browseBody = BrowseBody.builder( context ).continuation( continuation ).params( params ).build()

        return runCatching {
            val response = ytmBrowse( browseBody, Constants.JSON_HEADERS )
            val browseResponse = JSON.decodeFromString<BrowseResponseImpl>( response.responseBody )

            ContinuedPlaylistImpl.from(
                browseResponse.onResponseReceivedActions
                              .first()
                              .appendContinuationItemsAction
                              .continuationItems
            )
        }
    }

    fun browseArtist(
        artistId: String,
        localization: Localization,
        params: String?
    ): Result<InnertubeArtist> {
        val context = Context(
            Context.WEB_REMIX_DEFAULT.client.copy(
                hl = localization.languageCode,
                gl = localization.regionCode
            )
        )
        val browseBody = BrowseBody.builder( context ).browseId( artistId ).params( params ).build()

        return runCatching {
            val response = ytmBrowse( browseBody, Constants.JSON_HEADERS )
            val browseResponse = JSON.decodeFromString<BrowseResponseImpl>( response.responseBody )

            InnertubeArtistImpl.from( browseResponse )
        }
    }

    fun interface Provider {

        @Blocking
        @Throws(IOException::class)
        fun execute(request: Request): Response
    }
}