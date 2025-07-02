package me.knighthat.innertube.response


// Ripped from NewPipeExtractor
data class Response(
    val responseCode: Int,
    val responseMessage: String,
    val responseHeaders: Map<String, List<String>>,
    val responseBody: String
)