package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Badge
import me.knighthat.innertube.response.Icon

@Serializable
internal data class BadgeImpl(
    override val musicInlineBadgeRenderer: RendererImpl?,
    override val metadataBadgeRenderer: RendererImpl?
): Badge {

    @Serializable
    internal data class RendererImpl(
        override val style: String?,
        override val tooltip: String?,
        override val accessibilityData: AccessibilityImpl?,
        override val icon: IconImpl
    ): Badge.Renderer {

        @Serializable
        internal data class IconImpl(
            override val iconType: String
        ): Icon
    }
}
