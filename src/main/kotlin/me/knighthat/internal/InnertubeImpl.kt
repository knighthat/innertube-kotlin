package me.knighthat.internal

import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.http.parameters
import io.ktor.http.userAgent
import io.ktor.util.appendAll
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import me.knighthat.innertube.Constants
import me.knighthat.innertube.Endpoints
import me.knighthat.innertube.Innertube
import me.knighthat.innertube.PageType
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
import me.knighthat.innertube.model.Section
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.request.body.AccountMenuBody
import me.knighthat.innertube.request.body.BrowseBody
import me.knighthat.innertube.request.body.Builder
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.request.body.NextBody
import me.knighthat.innertube.request.body.PlayerBody
import me.knighthat.innertube.request.body.RequestBody
import me.knighthat.innertube.request.body.SearchBody
import me.knighthat.innertube.request.body.SearchSuggestionsBody
import me.knighthat.innertube.request.body.browse.TypeBuilder
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.Continuation
import me.knighthat.innertube.response.MusicPlaylistShelfRenderer
import me.knighthat.innertube.response.NextResponse
import me.knighthat.innertube.response.PlayerResponse
import me.knighthat.innertube.response.PlaylistPanelRenderer
import me.knighthat.innertube.response.SectionListRenderer
import me.knighthat.innertube.util.InnertubeUtils
import me.knighthat.internal.model.AccountInfoImpl
import me.knighthat.internal.model.ContinuedPlaylistImpl
import me.knighthat.internal.model.HomePageImpl
import me.knighthat.internal.model.InnertubeAlbumImpl
import me.knighthat.internal.model.InnertubeArtistImpl
import me.knighthat.internal.model.InnertubeChartsImpl
import me.knighthat.internal.model.InnertubePlaylistImpl
import me.knighthat.internal.model.InnertubeSearchImpl
import me.knighthat.internal.model.InnertubeSearchSuggestionImpl
import me.knighthat.internal.model.InnertubeSongDetailsImpl
import me.knighthat.internal.model.InnertubeSongImpl
import me.knighthat.internal.model.createInnertubeItemFrom
import me.knighthat.internal.model.createModelSectionFrom
import me.knighthat.internal.response.ActiveAccountHeaderRendererImpl
import me.knighthat.internal.response.BrowseResponseImpl
import me.knighthat.internal.response.NextResponseImpl
import me.knighthat.internal.response.PlayerResponseImpl
import me.knighthat.internal.response.SearchResponseImpl
import me.knighthat.internal.response.SearchSuggestionsResponseImpl
import me.knighthat.internal.util.getContext
import me.knighthat.internal.util.getSapisidHash
import org.intellij.lang.annotations.MagicConstant
import me.knighthat.innertube.request.body.next.Builder as NextBodyBuilder

internal class InnertubeImpl: Innertube {

    internal lateinit var provider: Innertube.KtorProvider

    internal suspend fun post(
        @MagicConstant(valuesFromClass = Endpoints::class)
        endpoint: String,
        request: RequestBody,
        headers: Map<String, List<String>> = emptyMap(),
        useLogin: Boolean = false
    ): HttpResponse {
        val client = request.context.client
        val host = client.originalUrl ?: Constants.YOUTUBE_MUSIC_URL

        return provider.client.post( "$host/$endpoint" ) {
            accept( ContentType.Application.Json )
            contentType( ContentType.Application.Json )

            // Disable pretty print - potentially save data
            url {
                parameters.append( "prettyPrint", "false" )
            }

            setBody( request )
            headers {
                userAgent( request.context.client.userAgent )

                append( "X-Origin", host )
                append( HttpHeaders.Referrer, "$host/" )

                append( "X-YouTube-Client-Name", client.xClientName.toString() )
                append( "X-YouTube-Client-Version", client.clientVersion )

                if( useLogin && "SAPISID" in provider.cookies ) {
                    val sapisidHash = getSapisidHash( provider.cookies, host )
                    if( sapisidHash.isNotBlank() ) {
                        append( HttpHeaders.Cookie, provider.cookies )
                        append(
                            name = HttpHeaders.Authorization,
                            value = "SAPISIDHASH $sapisidHash SAPISID1PHASH $sapisidHash SAPISID3PHASH $sapisidHash"
                        )
                    }
                }

                appendAll( headers )
            }
        }
    }

    @Throws(IOException::class)
    internal suspend fun ytmBrowse(
        localization: Localization,
        visitorData: String? = null,
        useLogin: Boolean = false,
        builder: TypeBuilder.() -> Builder<BrowseBody>
    ): BrowseResponse {
        val context = getContext( Context.WEB_REMIX_DEFAULT, localization, visitorData, useLogin )
        val body = BrowseBody.builder( context ).builder().build()

        return post( Endpoints.BROWSE, body ).body<BrowseResponseImpl>()
    }

    @Throws(IOException::class)
    internal suspend fun ytmNext(
        localization: Localization,
        template: Context = Context.WEB_REMIX_DEFAULT,
        visitorData: String? = null,
        useLogin: Boolean = false,
        builder: NextBodyBuilder.() -> Builder<NextBody>
    ): NextResponse {
        val context = getContext( template, localization, visitorData, useLogin )
        val body = NextBody.builder( context ).builder().build()

        return post( Endpoints.NEXT , body ).body<NextResponseImpl>()
    }

    override fun setProvider( provider: Innertube.KtorProvider ) {
        this.provider = provider
    }

    override suspend fun browsePlaylist(
        playlistId: String,
        localization: Localization,
        useLogin: Boolean
    ): Result<InnertubePlaylist> = runCatching {
        val browseResponse = ytmBrowse( localization, useLogin = useLogin ) {
            browseId( playlistId )
        }

        InnertubePlaylistImpl.from(
            browseResponse.responseContext.visitorData,
            browseResponse.contents!!.twoColumnBrowseResultsRenderer!!
        )
    }

    override suspend fun browsePlaylistSongs( playlistId: String, localization: Localization ): Result<List<InnertubeSong>> =
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

    override suspend fun playlistContinued(
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

    override suspend fun browseArtist( artistId: String, localization: Localization, params: String? ): Result<InnertubeArtist> =
        runCatching {
            val browseResponse = ytmBrowse( localization ) {
                browseId( artistId ).params( params )
            }

            InnertubeArtistImpl.from( browseResponse )
        }

    override suspend fun browseAlbum(
        albumId: String,
        localization: Localization,
        params: String?
    ): Result<InnertubeAlbum> =
        runCatching {
            val browseResponse = ytmBrowse( localization ) {
                browseId( albumId ).params( params )
            }

            InnertubeAlbumImpl.from( albumId, localization, browseResponse )
        }

    override suspend fun songBasicInfo( songId: String, localization: Localization, params: String? ): Result<InnertubeSong> =
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

    override suspend fun songInfo( songId: String, localization: Localization ): Result<InnertubeSongDetails> =
        runCatching {
            val nextResponse = ytmNext( localization, Context.WEB_DEFAULT, null, false ) {
                videoId( songId )
            }

            return@runCatching requireNotNull(
                nextResponse.contents
                    .twoColumnWatchNextResults
                    ?.results
                    ?.results
                    ?.contents
                    ?.let( InnertubeSongDetailsImpl::from )
            ) { "Failed to fetch details of $songId" }
        }

    override suspend fun radio(
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

    override suspend fun charts( localization: Localization, selectedValue: String, params: String? ): Result<InnertubeCharts> =
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

    override suspend fun accountInfo( localization: Localization ): Result<AccountInfoImpl> =
        runCatching {
            val context = getContext( Context.WEB_REMIX_DEFAULT, localization, provider.visitorData, true )
            val response = post( Endpoints.ACCOUNT_MENU, AccountMenuBody(context), useLogin = true ).body<JsonElement>()

            // This response is used here, and only here.
            // There's no need to make interfaces to parse
            val renderer = requireNotNull(
                response.jsonObject["actions"]
                        ?.jsonArray[0]
                        ?.jsonObject["openPopupAction"]
                        ?.jsonObject["popup"]
                        ?.jsonObject["multiPageMenuRenderer"]
                        ?.jsonObject["header"]
                        ?.jsonObject["activeAccountHeaderRenderer"]
            ) { "missing activeAccountHeaderRenderer while parsing accountInfo" }

            val json = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
            AccountInfoImpl.from(
                json.decodeFromJsonElement<ActiveAccountHeaderRendererImpl>( renderer )
            )
        }

    override suspend fun library( localization: Localization ): Result<List<InnertubeItem>> =
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

    override suspend fun player(
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
            val body = PlayerBody.builder( context )
                .videoId( songId )
                .apply {
                    signatureTimestamp?.also( ::signatureTimestamp )

                    if( context.client.xClientName != 5 ) return@apply

                    cpn(InnertubeUtils.randomString( 12 ) )

                    parameters {
                        append( "t", InnertubeUtils.randomString( 16 ) )
                        append( "id", songId )
                        append( "key", Constants.IOS_API_KEY )
                    }.formUrlEncode().also { endpoint += "?$it" }
                }
                .build()

            post( Endpoints.PLAYER, body, useLogin = useLogin ).body<PlayerResponseImpl>()
        }

    override suspend fun homePage( localization: Localization ): Result<HomePage> =
        runCatching {
            val response = ytmBrowse( localization ) {
                browseId( "FEmusic_home" )
            }

            return@runCatching HomePageImpl.from( response )
        }

    override suspend fun continuation(
        localization: Localization,
        visitorData: String?,
        continuation: String,
        params: String?
    ): Result<InnertubeContinuation> = runCatching {
        val response = ytmBrowse( localization, visitorData ) {
            continuation( continuation ).params( params )
        }

        val content = requireNotNull(
            response.continuationContents?.sectionListContinuation
        ) { "continuation doesn't contain any content" }
        val sections = requireNotNull(
            content.contents
                        .mapNotNull(SectionListRenderer.Content::musicCarouselShelfRenderer)
                        .map( ::createModelSectionFrom )
        ) { "failed to convert content to sections" }
        val continuation = content.continuations

        object : InnertubeContinuation {
            override val sections: List<Section> = sections
            override val continuations: List<Continuation> = continuation
            override val visitorData: String? = visitorData
        }
    }

    override suspend fun searchSuggestion(
        localization: Localization,
        input: String
    ): Result<InnertubeSearchSuggestion> =
        runCatching {
            val context = getContext( Context.WEB_REMIX_DEFAULT, localization, null, false )
            val body = SearchSuggestionsBody.builder( context ).input( input ).build()
            val response = post( Endpoints.SEARCH_SUGGESTIONS, body ).body<SearchSuggestionsResponseImpl>()

            InnertubeSearchSuggestionImpl.from( response )
        }

    override suspend fun search(
        localization: Localization,
        query: String,
        params: String?
    ): Result<InnertubeSearch> =
        runCatching {
            val context = getContext( Context.WEB_REMIX_DEFAULT, localization, null, false )
            val body: SearchBody = SearchBody.builder( context )
                                             .query( query )
                                             .params( params )
                                             .build()
            val response = post( Endpoints.SEARCH, body ).body<SearchResponseImpl>()

            InnertubeSearchImpl.from( response )
        }

    override suspend fun searchContinuation(
        localization: Localization,
        visitorData: String?,
        continuation: String
    ): Result<InnertubeContinuation> =
        runCatching {
            val context = getContext( Context.WEB_REMIX_DEFAULT, localization, null, false )
            val body: SearchBody = SearchBody.builder( context )
                                             .continuation( continuation )
                                             .build()
            val response = post( Endpoints.SEARCH, body ).body<BrowseResponseImpl>()

            val content = requireNotNull(
                response.continuationContents?.musicShelfContinuation
            ) { "continuation doesn't contain any content" }
            val section = requireNotNull(
                content.contents
                    .mapNotNull {
                        it.musicResponsiveListItemRenderer?.let( ::createInnertubeItemFrom )
                    }.let {
                        object : Section {
                            override val title: String? = null
                            override val accessibilityLabel: String? = null
                            override val browseId: String? = null
                            override val params: String? = null
                            override val contents: List<InnertubeItem> = it
                        }
                    }
            ) { "failed to convert content to sections" }
            val continuation = content.continuations

            object : InnertubeContinuation {
                override val sections: List<Section> = listOf( section )
                override val continuations: List<Continuation> = continuation
                override val visitorData: String? = visitorData
            }
        }
}