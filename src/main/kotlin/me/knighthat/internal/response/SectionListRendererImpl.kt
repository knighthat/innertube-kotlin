package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.SectionListRenderer
import me.knighthat.innertube.response.Thumbnail
import me.knighthat.innertube.response.Thumbnails

@Serializable
internal data class SectionListRendererImpl(
    override val contents: List<ContentImpl> = emptyList(),
    override val continuations: List<ContinuationImpl> = emptyList()
): SectionListRenderer {

    @Serializable
    internal data class ContentImpl(
        override val musicDescriptionShelfRenderer: MusicDescriptionShelfRendererImpl?,
        override val musicTastebuilderShelfRenderer: MusicTastebuilderShelfRendererImpl?,
        override val musicResponsiveHeaderRenderer: MusicResponsiveHeaderRendererImpl?,
        override val musicShelfRenderer: MusicShelfRendererImpl?,
        override val musicCarouselShelfRenderer: MusicCarouselShelfRendererImpl?,
        override val gridRenderer: GridRendererImpl?,
        override val musicPlaylistShelfRenderer: MusicPlaylistShelfRendererImpl?,
        override val musicCardShelfRenderer: MusicCardShelfRendererImpl?
    ): SectionListRenderer.Content {

        @Serializable
        data class MusicDescriptionShelfRendererImpl(
            override val header: Runs?,
            override val subheader: Runs?,
            override val description: Runs,
            override val shelfStyle: String?,
            override val maxCollapsedLines: Int?,
            override val maxExpandedLines: Int?,
            override val footer: Runs?
        ): SectionListRenderer.Content.MusicDescriptionShelfRenderer

        @Serializable
        internal data class MusicTastebuilderShelfRendererImpl(
            override val thumbnail: ThumbnailImpl,
            override val primaryText: Runs,
            override val secondaryText: Runs,
            override val isVisible: Boolean?
        ): SectionListRenderer.Content.MusicTastebuilderShelfRenderer {

            @Serializable
            internal data class ThumbnailImpl(
                override val musicTastebuilderShelfThumbnailRenderer: MusicTastebuilderShelfThumbnailRendererImpl
            ): SectionListRenderer.Content.MusicTastebuilderShelfRenderer.Thumbnail {

                @Serializable
                internal data class MusicTastebuilderShelfThumbnailRendererImpl(
                    override val thumbnail: Thumbnails
                ): SectionListRenderer.Content.MusicTastebuilderShelfRenderer.Thumbnail.MusicTastebuilderShelfThumbnailRenderer
            }
        }

        @Serializable
        internal data class MusicResponsiveHeaderRendererImpl(
            override val thumbnail: Thumbnail,
            override val title: RunsImpl,
            override val subtitle: RunsImpl,
            override val description: DescriptionImpl?,
            override val straplineTextOne: RunsImpl?,
            override val straplineThumbnail: Thumbnail?,
            override val subtitleBadge: List<BadgeImpl> = emptyList(),
            override val secondSubtitle: RunsImpl?
        ): SectionListRenderer.Content.MusicResponsiveHeaderRenderer {

            @Serializable
            internal data class DescriptionImpl(
                override val musicDescriptionShelfRenderer: MusicDescriptionShelfRendererImpl
            ): SectionListRenderer.Content.MusicResponsiveHeaderRenderer.Description
        }

        @Serializable
        internal data class GridRendererImpl(
            override val items: List<ItemImpl> = emptyList()
        ): SectionListRenderer.Content.GridRenderer {

            @Serializable
            internal data class ItemImpl(
                override val musicTwoRowItemRenderer: MusicTwoRowItemRendererImpl
            ): SectionListRenderer.Content.GridRenderer.Item
        }
    }
}