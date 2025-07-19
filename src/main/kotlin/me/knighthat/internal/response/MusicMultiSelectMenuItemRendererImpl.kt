package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicMultiSelectMenuItemRenderer

@Serializable
internal data class MusicMultiSelectMenuItemRendererImpl(
    override val musicMultiSelectMenuRenderer: MusicMultiSelectMenuRendererImpl
) : MusicMultiSelectMenuItemRenderer {

    @Serializable
    internal data class MusicMultiSelectMenuRendererImpl(
        override val title: TitleImpl,
        override val options: List<OptionImpl>
    ) : MusicMultiSelectMenuItemRenderer.MusicMultiSelectMenuRenderer {

        @Serializable
        internal data class TitleImpl(
            override val musicMenuTitleRenderer: RendererImpl
        ) : MusicMultiSelectMenuItemRenderer.MusicMultiSelectMenuRenderer.Title {

            @Serializable
            internal data class RendererImpl(
                override val primaryText: RunsImpl
            ) : MusicMultiSelectMenuItemRenderer.MusicMultiSelectMenuRenderer.Title.Renderer
        }

        @Serializable
        internal data class OptionImpl(
            override val musicMultiSelectMenuItemRenderer: ItemRendererImpl?,
            override val musicMenuItemDividerRenderer: DividerRendererImpl?
        ) : MusicMultiSelectMenuItemRenderer.MusicMultiSelectMenuRenderer.Option {

            @Serializable
            internal data class ItemRendererImpl(
                override val title: RunsImpl,
                override val formItemEntityKey: String,
                override val selectedIcon: IconImpl,
                override val selectedAccessibility: AccessibilityImpl,
                override val deselectedAccessibility: AccessibilityImpl
            ) : MusicMultiSelectMenuItemRenderer.MusicMultiSelectMenuRenderer.Option.ItemRenderer

            @Serializable
            internal data class DividerRendererImpl(
                override val hack: Boolean
            ) : MusicMultiSelectMenuItemRenderer.MusicMultiSelectMenuRenderer.Option.DividerRenderer
        }
    }
}