package me.knighthat.innertube

import me.knighthat.innertube.request.Request
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.request.body.NextBody
import me.knighthat.innertube.request.body.SearchSuggestionsBody
import me.knighthat.innertube.response.Response
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.MediaType
import java.io.BufferedReader
import java.io.IOException
import java.util.zip.GZIPInputStream


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
                Request.POST, Constants.YOUTUBE_URL, Endpoints.NEXT, body, Constants.JSON_HEADERS
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
                Request.POST, Constants.YOUTUBE_MUSIC_URL, Endpoints.NEXT, body, Constants.JSON_HEADERS
            )
        }

        assertEquals( 200, response.responseCode )
        assertFalse( response.responseBody.isBlank() )
    }

    class InnertubeProvider: Innertube.Provider {

        val CLIENT: OkHttpClient =
            OkHttpClient.Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor().setLevel( HttpLoggingInterceptor.Level.HEADERS )
                        )
                        .addInterceptor(
                            HttpLoggingInterceptor().setLevel( HttpLoggingInterceptor.Level.BODY )
                        )
                        .build()
        override val visitorData: String = Constants.VISITOR_DATA

        @Throws(IOException::class)
        override fun execute( request: Request ): Response {
            val builder = okhttp3.Request.Builder()

            // Add headers
            request.headers
                   .mapValues { it.value.joinToString() }
                   .forEach( builder::addHeader )

            // Destination (with prettyPrint turned off)
            builder.url(
                url = request.url
                             .toHttpUrl()
                             .newBuilder()
                             .addQueryParameter( "prettyPrint", "false" )
                             .build()
            )

            // Add method and payload (body)
            when ( request.httpMethod ) {
                Request.GET     -> builder.get()
                Request.POST    -> {
                    builder.post(
                        body = JSON.encodeToString( request.dataToSend )
                                   .toRequestBody( MediaType.APPLICATION_JSON.toString().toMediaType() )
                    )
                }

                else            -> throw UnsupportedOperationException("Unknown method ${request.httpMethod}")
            }


            val result = CLIENT.newCall( builder.build() ).execute().use { response ->
                Response(
                    response.code,
                    response.message,
                    response.headers.toMultimap(),
                    GZIPInputStream( response.body!!.byteStream() ).bufferedReader().use(BufferedReader::readText )
                )
            }
            return result
        }
    }
}