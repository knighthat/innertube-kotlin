package me.knighthat.innertube.response

import kotlinx.serialization.json.Json

object JsonParser {

    val JSON: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}