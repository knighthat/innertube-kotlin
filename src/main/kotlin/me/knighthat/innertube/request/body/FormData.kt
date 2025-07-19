package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable

@Serializable
data class FormData(
    val selectedValues: List<String>
)