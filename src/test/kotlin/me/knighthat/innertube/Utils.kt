package me.knighthat.innertube

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream

@JvmField
val JSON: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> InputStream.decode(): T? = JSON.decodeFromStream( this )