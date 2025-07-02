package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Accessibility

@Serializable
internal data class AccessibilityImpl(
    override val accessibilityData: DataImpl
): Accessibility {

    @Serializable
    internal data class DataImpl(
        override val label: String
    ): Accessibility.Data
}