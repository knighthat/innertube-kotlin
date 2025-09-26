package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Badge

@Serializable
internal data class BadgeImpl(
    override val musicInlineBadgeRenderer: MusicInlineBadgeImpl?,
    override val metadataBadgeRenderer: MetadataBadgeImpl?
): Badge {

    @Serializable
    data class MusicInlineBadgeImpl(
        override val accessibilityData: AccessibilityImpl?,
        override val style: String?,
        override val tooltip: String?,
        override val icon: IconImpl?
    ): Badge.MusicInlineBadge

    @Serializable
    data class MetadataBadgeImpl(
        override val accessibilityData: AccessibilityImpl.DataImpl?,
        override val style: String?,
        override val tooltip: String?,
        override val icon: IconImpl?
    ): Badge.MetadataBadge
}
