package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Badge

@Serializable
internal data class BadgeImpl(
    override val musicInlineBadgeRenderer: RendererImpl?,
    override val metadataBadgeRenderer: RendererImpl?
): Badge {

    @Serializable
    internal data class RendererImpl(
        override val style: String?,
        override val tooltip: String?,
        override val accessibilityData: AccessibilityImpl?
    ): Badge.Renderer
}
