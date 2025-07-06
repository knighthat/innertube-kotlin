package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable

@Serializable
sealed interface RequestBody {

    val context: Context
}