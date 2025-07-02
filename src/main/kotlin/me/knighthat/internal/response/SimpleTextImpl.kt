package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.SimpleText

@Serializable
internal data class SimpleTextImpl(
    override val simpleText: String,
    override val accessibility: AccessibilityImpl?
): SimpleText