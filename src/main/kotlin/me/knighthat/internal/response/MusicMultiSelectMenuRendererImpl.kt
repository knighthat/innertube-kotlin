package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicMultiSelectMenuRenderer

@Serializable
internal data class MusicMultiSelectMenuRendererImpl(
    override val title: TitleImpl,
    override val options: List<OptionImpl>
) : MusicMultiSelectMenuRenderer {

    @Serializable
    internal data class TitleImpl(
        override val musicMenuTitleRenderer: RendererImpl
    ) : MusicMultiSelectMenuRenderer.Title {

        @Serializable
        internal data class RendererImpl(
            override val primaryText: RunsImpl
        ) : MusicMultiSelectMenuRenderer.Title.Renderer
    }

    @Serializable
    internal data class OptionImpl(
        override val musicMultiSelectMenuItemRenderer: ItemRendererImpl?,
        override val musicMenuItemDividerRenderer: DividerRendererImpl?
    ) : MusicMultiSelectMenuRenderer.Option {

        @Serializable
        internal data class ItemRendererImpl(
            override val title: RunsImpl,
            override val formItemEntityKey: String,
            override val selectedAccessibility: AccessibilityImpl,
            override val deselectedAccessibility: AccessibilityImpl
        ) : MusicMultiSelectMenuRenderer.Option.ItemRenderer

        @Serializable
        internal data class DividerRendererImpl(
            override val hack: Boolean
        ) : MusicMultiSelectMenuRenderer.Option.DividerRenderer
    }
}