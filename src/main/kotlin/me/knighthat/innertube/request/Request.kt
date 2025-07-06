package me.knighthat.innertube.request

import me.knighthat.innertube.request.Request.Companion.GET
import me.knighthat.innertube.request.Request.Companion.POST
import me.knighthat.innertube.request.body.RequestBody
import org.intellij.lang.annotations.MagicConstant

/**
 * Simple class represents the data to be sent.
 *
 * Params' description is copied from [mdn web docs](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/)
 *
 * [dataToSend] should be in Json format.
 *
 * @param httpMethod Indicate the purpose of the request, use [GET] or [POST]
 * @param headers Pass additional information
 * @param url Where to send this request to
 * @param dataToSend Content of request, can be `null` if [httpMethod] is **GET**
 */
// Ripped from NewPipeExtractor
data class Request(
    @MagicConstant(valuesFromClass = Request::class)
    val httpMethod: String? = null,
    val headers: Map<String, List<String>>,
    val url: String,
    val dataToSend: RequestBody?
) {

    companion object {

        const val GET: String = "GET"
        const val POST: String = "POST"
    }
}
