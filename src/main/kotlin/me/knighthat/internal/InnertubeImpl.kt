package me.knighthat.internal

import io.ktor.http.formUrlEncode
import io.ktor.http.parameters
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import me.knighthat.innertube.Constants
import me.knighthat.innertube.Endpoints
import me.knighthat.innertube.Innertube
import me.knighthat.innertube.PageType
import me.knighthat.innertube.UserAgents
import me.knighthat.innertube.model.ContinuedPlaylist
import me.knighthat.innertube.model.HomePage
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeCharts
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.InnertubePlaylist
import me.knighthat.innertube.model.InnertubeSong
import me.knighthat.innertube.model.InnertubeSongDetails
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.request.Request
import me.knighthat.innertube.request.body.AccountMenuBody
import me.knighthat.innertube.request.body.BrowseBody
import me.knighthat.innertube.request.body.Builder
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.request.body.NextBody
import me.knighthat.innertube.request.body.PlayerBody
import me.knighthat.innertube.request.body.RequestBody
import me.knighthat.innertube.request.body.browse.TypeBuilder
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.MusicPlaylistShelfRenderer
import me.knighthat.innertube.response.NextResponse
import me.knighthat.innertube.response.PlayerResponse
import me.knighthat.innertube.response.PlaylistPanelRenderer
import me.knighthat.innertube.response.Response
import me.knighthat.innertube.response.SectionListRenderer
import me.knighthat.internal.model.AccountInfoImpl
import me.knighthat.internal.model.ContinuedPlaylistImpl
import me.knighthat.internal.model.HomePageImpl
import me.knighthat.internal.model.InnertubeAlbumImpl
import me.knighthat.internal.model.InnertubeArtistImpl
import me.knighthat.internal.model.InnertubeChartsImpl
import me.knighthat.internal.model.InnertubePlaylistImpl
import me.knighthat.internal.model.InnertubeSongDetailsImpl
import me.knighthat.internal.model.InnertubeSongImpl
import me.knighthat.internal.response.ActiveAccountHeaderRendererImpl
import me.knighthat.internal.response.BrowseResponseImpl
import me.knighthat.internal.response.NextResponseImpl
import me.knighthat.internal.response.PlayerResponseImpl
import org.intellij.lang.annotations.MagicConstant
import me.knighthat.innertube.request.body.next.Builder as NextBodyBuilder

internal class InnertubeImpl: Innertube {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    lateinit var client: Innertube.Provider

    private fun randomString(
        length: Int,
        allowedCharset: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    ): String =
        String(CharArray(length) { allowedCharset.random() })

    private fun getContext( template: Context, localization: Localization, visitorData: String?, useLogin: Boolean ): Context {
        val visitorData = if( useLogin )
            client.visitorData
        else visitorData ?: template.client.userAgent

        return Context(
            template.client.copy(
                hl = localization.languageCode,
                gl = localization.regionCode,
                visitorData = visitorData
            ),
            Context.User().copy(
                onBehalfOfUser = if( useLogin ) client.dataSyncId else null
            )
        )
    }


    private fun appendUserAgent(
        headers: Map<String, List<String>>,
        userAgent: String = UserAgents.CHROME_WINDOWS
    ): Map<String, List<String>> =
        headers.toMutableMap().apply {
            putIfAbsent( "User-Agent", listOf( userAgent ) )
        }

    @Throws(IOException::class)
    @JvmName("sendRequest")
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

    @Throws(IOException::class)
    internal fun ytmBrowse(
        localization: Localization,
        visitorData: String? = null,
        useLogin: Boolean = false,
        headers: Map<String, List<String>> = emptyMap(),
        builder: TypeBuilder.() -> Builder<BrowseBody>
    ): BrowseResponse {
        val context = getContext( Context.WEB_REMIX_DEFAULT, localization, visitorData, useLogin )
        val browseBody = BrowseBody.builder( context ).builder().build()
        val response = sendRequest(
            Request.POST,
            Constants.YOUTUBE_MUSIC_URL,
            Endpoints.BROWSE,
            browseBody,
            appendUserAgent( headers ),
            useLogin
        )

        return json.decodeFromString<BrowseResponseImpl>( response.responseBody )
    }

    @Throws(IOException::class)
    internal fun ytmNext(
        localization: Localization,
        visitorData: String? = null,
        useLogin: Boolean = false,
        headers: Map<String, List<String>> = emptyMap(),
        builder: NextBodyBuilder.() -> Builder<NextBody>
    ): NextResponse {
        val context = getContext( Context.WEB_REMIX_DEFAULT, localization, visitorData, useLogin )
        val nextBody = NextBody.builder( context ).builder().build()
        val response = sendRequest(
            Request.POST,
            Constants.YOUTUBE_MUSIC_URL,
            Endpoints.NEXT,
            nextBody,
            appendUserAgent( headers ),
            useLogin
        )

        return json.decodeFromString<NextResponseImpl>( response.responseBody )
    }

    override fun setProvider( provider: Innertube.Provider ) { this.client = provider }

    override fun browsePlaylist( playlistId: String, localization: Localization, useLogin: Boolean): Result<InnertubePlaylist> =
        runCatching {
            val browseResponse = ytmBrowse( localization, useLogin = useLogin ) {
                browseId( playlistId )
            }

            InnertubePlaylistImpl.from(
                browseResponse.responseContext.visitorData,
                browseResponse.contents!!.twoColumnBrowseResultsRenderer!!
            )
        }

    override fun browsePlaylistSongs( playlistId: String, localization: Localization ): Result<List<InnertubeSong>> =
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

    override fun playlistContinued(
        visitorData: String?,
        continuation: String,
        localization: Localization,
        params: String?,
        useLogin: Boolean
    ): Result<ContinuedPlaylist> =
        runCatching {
            val browseResponse = ytmBrowse( localization, visitorData, useLogin ) {
                continuation( continuation ).params( params )
            }

            ContinuedPlaylistImpl.from(
                browseResponse.onResponseReceivedActions
                              .first()
                              .appendContinuationItemsAction
                              .continuationItems
            )
        }

    override fun browseArtist( artistId: String, localization: Localization, params: String? ): Result<InnertubeArtist> =
        runCatching {
            val browseResponse = ytmBrowse( localization ) {
                browseId( artistId ).params( params )
            }

            InnertubeArtistImpl.from( browseResponse )
        }

    override fun browseAlbum(
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

    override fun songBasicInfo( songId: String, localization: Localization, params: String? ): Result<InnertubeSong> =
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
            ) { "missing playlistPanelVideoRenderer while parsing songBasicInfo" }

            InnertubeSongImpl.from( renderer )
        }

    override fun songInfo( songId: String, localization: Localization ): Result<InnertubeSongDetails> =
        runCatching {
            val context = getContext( Context.WEB_DEFAULT, localization, null, false )
            val nextBody = NextBody.builder( context ).videoId( songId ).build()
            val response = sendRequest(
                Request.POST,
                Constants.YOUTUBE_URL,
                Endpoints.NEXT,
                nextBody,
                emptyMap(),
                false
            )
            val nextResponse = json.decodeFromString<NextResponseImpl>( response.responseBody )

            return@runCatching requireNotNull(
                nextResponse.contents
                    .twoColumnWatchNextResults
                    ?.results
                    ?.results
                    ?.contents
                    ?.let( InnertubeSongDetailsImpl::from )
            ) { "Failed to fetch details of $songId" }
        }

    override fun radio(
        songId: String,
        localization: Localization,
        playlistId: String,
        params: String?,
        includeProvidedSong: Boolean
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

    override fun charts( localization: Localization, selectedValue: String, params: String? ): Result<InnertubeCharts> =
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
            ) { "missing sectionListRenderer while parsing charts" }

            InnertubeChartsImpl.from(renderer)
        }

    override fun accountInfo( localization: Localization ): Result<AccountInfoImpl> =
        runCatching {
            val context = Context(
                Context.WEB_REMIX_DEFAULT.client.copy(
                    hl = localization.languageCode,
                    gl = localization.regionCode,
                    visitorData = client.visitorData
                ),
                Context.User().copy(
                    onBehalfOfUser = client.dataSyncId
                )
            )
            val response = sendRequest(
                Request.POST,
                Constants.YOUTUBE_MUSIC_URL,
                Endpoints.ACCOUNT_MENU,
                AccountMenuBody(context),
                mapOf( "User-Agent" to listOf( UserAgents.CHROME_WINDOWS ) ),
                true
            )

            // This response is used here, and only here.
            // There's no need to make interfaces to parse
            val renderer = requireNotNull(
                json.parseToJsonElement( response.responseBody )
                    .jsonObject["actions"]
                    ?.jsonArray[0]
                    ?.jsonObject["openPopupAction"]
                    ?.jsonObject["popup"]
                    ?.jsonObject["multiPageMenuRenderer"]
                    ?.jsonObject["header"]
                    ?.jsonObject["activeAccountHeaderRenderer"]
            ) { "missing activeAccountHeaderRenderer while parsing accountInfo" }
            AccountInfoImpl.from(
                json.decodeFromJsonElement<ActiveAccountHeaderRendererImpl>( renderer )
            )
        }

    override fun library( localization: Localization ): Result<List<InnertubeItem>> =
        runCatching {
            val response = ytmBrowse( localization, useLogin = true ) {
                browseId( "FEmusic_library_landing" )
            }

            response.contents
                    ?.singleColumnBrowseResultsRenderer
                    ?.tabs
                    ?.firstOrNull()
                    ?.tabRenderer
                    ?.content
                    ?.sectionListRenderer
                    ?.contents
                    ?.firstOrNull()
                    ?.gridRenderer
                    ?.items
                    ?.map(SectionListRenderer.Content.GridRenderer.Item::musicTwoRowItemRenderer )
                    ?.mapNotNull { renderer ->
                        val pageType: String? = renderer.navigationEndpoint
                                                        .browseEndpoint
                                                        ?.browseEndpointContextSupportedConfigs
                                                        ?.browseEndpointContextMusicConfig
                                                        ?.pageType

                        when( pageType ) {
                            PageType.ARTIST   -> InnertubeArtistImpl.from( renderer )
                            PageType.ALBUM    -> InnertubeAlbumImpl.from( renderer )
                            PageType.PLAYLIST -> InnertubePlaylistImpl.from( renderer )
                            else              -> null
                        }
                    }
                    .orEmpty()
        }

    override fun player(
        songId: String,
        context: Context,
        localization: Localization,
        signatureTimestamp: Int?,
        visitorData: String?,
        useLogin: Boolean
    ): Result<PlayerResponse> =
        runCatching {
            var endpoint = Endpoints.PLAYER

            val context = getContext( context, localization, visitorData, useLogin )
            val playerBody = PlayerBody.builder( context )
                .videoId( songId )
                .apply {
                    signatureTimestamp?.also( ::signatureTimestamp )

                    if( context.client.xClientName != 5 ) return@apply

                    cpn( randomString( 12 ) )

                    parameters {
                        append( "t", randomString( 16 ) )
                        append( "id", songId )
                        append( "key", Constants.IOS_API_KEY )
                    }.formUrlEncode().also { endpoint += "?$it" }
                }
                .build()

            val response = sendRequest(
                method = Request.POST,
                host = context.client.originalUrl ?: Constants.YOUTUBE_MUSIC_URL,
                endpoint = endpoint,
                requestBody = playerBody,
                headers = emptyMap(),
                useLogin = useLogin
            )
            json.decodeFromString<PlayerResponseImpl>( response.responseBody )
        }

    override fun homePage( localization: Localization ): Result<HomePage> =
        runCatching {
            val response = ytmBrowse( localization ) {
                browseId( "FEmusic_home" )
            }

            return@runCatching HomePageImpl.from( response )
        }
}