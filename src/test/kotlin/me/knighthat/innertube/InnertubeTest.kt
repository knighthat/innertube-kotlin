package me.knighthat.innertube

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import me.knighthat.innertube.request.Request
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.request.body.NextBody
import me.knighthat.innertube.request.body.SearchSuggestionsBody
import me.knighthat.innertube.response.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.IOException


class InnertubeTest {

    companion object {

        @BeforeAll
        @JvmStatic
        fun setup() { Innertube.client = InnertubeProvider() }
    }

    @Test
    fun testSendRequestToYouTube() {
        // Fabricating & sending request within [Assertions.assertDoesNotThrow]
        // to avoid try/catch
        val response = assertDoesNotThrow {
            val body: NextBody = NextBody.builder( Context.WEB_DEFAULT )
                                         .videoId( "lYBUbBu4W08" )
                                         .build()
            Innertube.sendRequest(
                Request.POST,
                Constants.YOUTUBE_URL,
                Endpoints.NEXT,
                body,
                emptyMap(),
                false
            )
        }

        assertEquals( 200, response.responseCode )
        assertFalse( response.responseBody.isBlank() )
    }

    @Test
    fun testSendRequestToYouTubeMusic() {
        val response = assertDoesNotThrow {
            val body: SearchSuggestionsBody = SearchSuggestionsBody.builder( Context.WEB_REMIX_DEFAULT )
                                                                   .input( "million dollar baby" )
                                                                   .build()
            Innertube.sendRequest(
                Request.POST,
                Constants.YOUTUBE_MUSIC_URL,
                Endpoints.NEXT,
                body,
                emptyMap(),
                false
            )
        }

        assertEquals( 200, response.responseCode )
        assertFalse( response.responseBody.isBlank() )
    }

    class InnertubeProvider: Innertube.Provider {

        val client = HttpClient( OkHttp ) {
            expectSuccess = true

            install(ContentNegotiation ) {
                json()
            }

            install( ContentEncoding ) {
                gzip( 1f )
                deflate( .1f )
            }

            engine {
                val interceptor = HttpLoggingInterceptor().setLevel( HttpLoggingInterceptor.Level.BODY )
                interceptor.redactHeader( "Cookie" )

                addInterceptor( interceptor )
            }
        }
        override val cookies: String = ""
        override val dataSyncId: String? = null
        override val visitorData: String = Constants.CHROME_WINDOWS_VISITOR_DATA

        @Throws(IOException::class)
        override fun execute( request: Request ): Response = runBlocking( Dispatchers.IO ) {
            val result = client.request( request.url ) {
                accept( ContentType.Application.Json )
                contentType( ContentType.Application.Json )
                method = HttpMethod.parse( request.httpMethod )

                // Disable pretty print - potentially save data
                url {
                    parameters.append( "prettyPrint", "false" )
                }
                // Only setBody when it's not null
                request.dataToSend?.also( this::setBody )
                // Add headers
                request.headers.forEach( headers::appendAll )

                headers {
                    append( "X-Goog-Api-Format-Version", "1" )
                    append(
                        "X-Origin",
                        request.dataToSend?.context?.client?.originalUrl
                            ?: "${url.protocol.name}://${url.host}" )
                    append(
                        "Referer",
                        request.dataToSend?.context?.client?.referer
                            ?: "${url.protocol.name}://${url.host}"
                    )

                    val context = request.dataToSend?.context ?: Context.WEB_REMIX_DEFAULT

                    append( "X-YouTube-Client-Name", context.client.xClientName.toString() )
                    append( "X-YouTube-Client-Version", context.client.clientVersion )
                }
            }

            Response(
                result.status.value, "", result.headers.toMap(), result.bodyAsText()
            )
        }
    }
}