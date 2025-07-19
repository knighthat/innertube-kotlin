package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Icon

@Serializable
internal data class IconImpl(
    override val iconType: String
): Icon
