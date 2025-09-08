package me.knighthat.innertube

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import me.knighthat.innertube.request.Request
import me.knighthat.innertube.request.body.Context
import me.knighthat.innertube.response.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

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