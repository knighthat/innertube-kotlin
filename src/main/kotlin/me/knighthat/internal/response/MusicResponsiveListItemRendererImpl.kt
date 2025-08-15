package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer

@Serializable
internal data class MusicResponsiveListItemRendererImpl(
    override val thumbnail: ThumbnailImpl?,
    override val overlay: OverlayImpl?,
    override val flexColumns: List<ColumImpl> = emptyList(),
    override val fixedColumns: List<ColumImpl> = emptyList(),
    override val badges: List<BadgeImpl> = emptyList(),
    override val playlistItemData: PlaylistItemDataImpl?,
    override val flexColumnDisplayStyle: String?,
    override val navigationEndpoint: EndpointImpl?,
    override val itemHeight: String?,
    override val index: RunsImpl?,
    override val customIndexColumn: CustomIndexColumnImpl?
): MusicResponsiveListItemRenderer {

    @Serializable
    internal data class ColumImpl(
        override val musicResponsiveListItemFlexColumnRenderer: RendererImpl?,
        override val musicResponsiveListItemFixedColumnRenderer: RendererImpl?
    ): MusicResponsiveListItemRenderer.Colum {

        @Serializable
        internal data class RendererImpl(
            override val text: RunsImpl?,
            override val displayPriority: String,
            override val size: String?
        ): MusicResponsiveListItemRenderer.Colum.Renderer
    }

    @Serializable
    internal data class PlaylistItemDataImpl(
        override val playlistSetVideoId: String?,
        override val videoId: String?
    ): MusicResponsiveListItemRenderer.PlaylistItemData

    @Serializable
    internal data class CustomIndexColumnImpl(
        override val musicCustomIndexColumnRenderer: RendererImpl
    ) : MusicResponsiveListItemRenderer.CustomIndexColumn {

        @Serializable
        internal data class RendererImpl(
            override val text: RunsImpl,
            override val icon: IconImpl?,
            override val accessibilityData: AccessibilityImpl
        ) : MusicResponsiveListItemRenderer.CustomIndexColumn.Renderer
    }
}