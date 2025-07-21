package me.knighthat.innertube

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeCharts
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.request.Request
import me.knighthat.innertube.request.body.BrowseBody
import me.knighthat.innertube.request.body.Builder
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.request.body.NextBody
import me.knighthat.innertube.request.body.RequestBody
import me.knighthat.innertube.request.body.browse.TypeBuilder
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.MusicPlaylistShelfRenderer
import me.knighthat.innertube.response.NextResponse
import me.knighthat.innertube.response.PlaylistPanelRenderer
import me.knighthat.innertube.response.Response
import me.knighthat.internal.model.ContinuedPlaylistImpl
import me.knighthat.internal.model.InnertubeAlbumImpl
import me.knighthat.internal.model.InnertubeArtistImpl
import me.knighthat.internal.model.InnertubeChartsImpl
import me.knighthat.internal.model.InnertubePlaylistImpl
import me.knighthat.internal.model.InnertubeSongImpl
import me.knighthat.internal.response.BrowseResponseImpl
import me.knighthat.internal.response.NextResponseImpl
import org.intellij.lang.annotations.MagicConstant
import org.jetbrains.annotations.Blocking
import org.jetbrains.annotations.VisibleForTesting
import java.io.IOException


object Innertube {

    private val JSON: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    lateinit var client: Provider

    @VisibleForTesting
    @Blocking
    @Throws(IOException::class)
    internal fun sendRequest(
        @MagicConstant(valuesFromClass = Request::class) method: String,
        host: String,
        @MagicConstant(valuesFromClass = Endpoints::class) endpoint: String,
        requestBody: RequestBody,
        headers: Map<String, List<String>>,
        useLogin: Boolean
    ): Response = client.execute(
        Request(method, headers, "$host/$endpoint", useLogin, requestBody)
    )

    @VisibleForTesting
    @Throws(IOException::class)
    internal fun ytmBrowse(
        localization: Localization,
        visitorData: String = client.visitorData,
        useLogin: Boolean = false,
        builder: TypeBuilder.() -> Builder<BrowseBody>
    ): BrowseResponse {
        val context = Context(
            Context.WEB_REMIX_DEFAULT.client.copy(
                hl = localization.languageCode,
                gl = localization.regionCode,
                visitorData = visitorData
            )
        )
        val browseBody = BrowseBody.builder( context ).builder().build()
        val response = sendRequest(
            Request.POST,
            Constants.YOUTUBE_MUSIC_URL,
            Endpoints.BROWSE,
            browseBody,
            Constants.JSON_HEADERS,
            useLogin
        )

        return JSON.decodeFromString<BrowseResponseImpl>( response.responseBody )
    }

    @VisibleForTesting
    @Throws(IOException::class)
    internal fun ytmNext(
        localization: Localization,
        visitorData: String = client.visitorData,
        useLogin: Boolean = false,
        builder: me.knighthat.innertube.request.body.next.Builder.() -> Builder<NextBody>
    ): NextResponse {
        val context = Context(
            Context.WEB_REMIX_DEFAULT.client.copy(
                hl = localization.languageCode,
                gl = localization.regionCode,
                visitorData = visitorData
            )
        )
        val nextBody = NextBody.builder( context ).builder().build()
        val response = sendRequest(
            Request.POST,
            Constants.YOUTUBE_MUSIC_URL,
            Endpoints.NEXT,
            nextBody,
            Constants.JSON_HEADERS,
            useLogin
        )

        return JSON.decodeFromString<NextResponseImpl>( response.responseBody )
    }

    fun browsePlaylist(
        playlistId: String,
        localization: Localization
    ): Result<InnertubePlaylist> =
        runCatching {
            val browseResponse = ytmBrowse( localization ) { browseId( playlistId ) }

            InnertubePlaylistImpl.from(
                browseResponse.responseContext.visitorData!!,
                browseResponse.contents!!.twoColumnBrowseResultsRenderer!!
            )
        }

    fun browsePlaylistSongs(
        playlistId: String,
        localization: Localization
    ): Result<List<InnertubeSong>> =
        runCatching {
            val browseResponse = ytmBrowse( localization ) { browseId( playlistId ) }

            browseResponse.contents
                          ?.twoColumnBrowseResultsRenderer
                          ?.secondaryContents
                          ?.sectionListRenderer
                          ?.contents
                          ?.first()
                          ?.musicPlaylistShelfRenderer
                          ?.contents
                          ?.mapNotNull( MusicPlaylistShelfRenderer.Content::musicResponsiveListItemRenderer )
                          ?.map( InnertubeSongImpl::from )
                          .orEmpty()
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
    ): Result<ContinuedPlaylist> =
        runCatching {
            val browseResponse = ytmBrowse( localization, visitorData ) {
                continuation( continuation ).params( params )
            }

            ContinuedPlaylistImpl.from(
                browseResponse.onResponseReceivedActions
                              .first()
                              .appendContinuationItemsAction
                              .continuationItems
            )
        }

    fun browseArtist(
        artistId: String,
        localization: Localization,
        params: String?
    ): Result<InnertubeArtist> =
        runCatching {
            val browseResponse = ytmBrowse( localization ) {
                browseId( artistId ).params( params )
            }

            InnertubeArtistImpl.from( browseResponse )
        }

    fun browseAlbum(
        albumId: String,
        localization: Localization,
        params: String?
    ): Result<InnertubeAlbum> =
        runCatching {
            val browseResponse = ytmBrowse( localization ) {
                browseId( albumId ).params( params )
            }

            runBlocking {
                InnertubeAlbumImpl.from( albumId, localization, browseResponse )
            }
        }

    fun songBasicInfo(
        songId: String,
        localization: Localization,
        params: String? = null
    ): Result<InnertubeSong> =
        runCatching {
            val nextResponse = ytmNext( localization ) {
                videoId( songId ).params( params )
            }
            val renderer = requireNotNull(
                nextResponse.contents
                            .singleColumnMusicWatchNextResultsRenderer
                            ?.tabbedRenderer
                            ?.watchNextTabbedResultsRenderer
                            ?.tabs
                            ?.firstOrNull()
                            ?.tabRenderer
                            ?.content
                            ?.musicQueueRenderer
                            ?.content
                            ?.playlistPanelRenderer
                            ?.contents
                            ?.first()
                            ?.playlistPanelVideoRenderer
            )

            InnertubeSongImpl.from( renderer )
        }

    fun radio(
        songId: String,
        localization: Localization,
        playlistId: String = "RDAMVM$songId",
        params: String? = null,
        includeProvidedSong: Boolean = false
    ): Result<List<InnertubeSong>> =
        runCatching {
            val nextResponse = ytmNext( localization ) {
                if( includeProvidedSong )
                    videoId( songId )

                playlistId( playlistId ).params( params )
            }

            nextResponse.contents
                        .singleColumnMusicWatchNextResultsRenderer
                        ?.tabbedRenderer
                        ?.watchNextTabbedResultsRenderer
                        ?.tabs
                        ?.firstOrNull()
                        ?.tabRenderer
                        ?.content
                        ?.musicQueueRenderer
                        ?.content
                        ?.playlistPanelRenderer
                        ?.contents
                        ?.mapNotNull( PlaylistPanelRenderer.Content::playlistPanelVideoRenderer )
                        ?.map( InnertubeSongImpl::from )
                        .orEmpty()
        }

    fun charts(
        localization: Localization,
        params: String?,
        selectedValue: String
    ): Result<InnertubeCharts> =
        runCatching {
            val browseResponse = ytmBrowse(localization) {
                browseId("FEmusic_charts").params(params).formData(selectedValue)
            }
            val renderer = requireNotNull(
                browseResponse.contents
                    ?.singleColumnBrowseResultsRenderer
                    ?.tabs
                    ?.firstOrNull()
                    ?.tabRenderer
                    ?.content
                    ?.sectionListRenderer
            )

            InnertubeChartsImpl.from(renderer)
        }

    interface Provider {

        val visitorData: String

        @Blocking
        @Throws(IOException::class)
        fun execute( request: Request ): Response
    }
}