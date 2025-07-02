package me.knighthat.internal.response

import me.knighthat.innertube.response.*

data class PlaylistPanelRendererImpl(
    override val contents: List<ContentImpl> = emptyList(),
    override val playlistId: String,
    override val isInfinite: Boolean,
    override val continuations: List<ContinuationImpl> = emptyList(),
    override val numItemsToShow: Short
): PlaylistPanelRenderer {

    data class ContentImpl(
        override val playlistPanelVideoRenderer: PlaylistPanelRenderer.Content.VideoRenderer?
    ): PlaylistPanelRenderer.Content {

        data class VideoRendererImpl(
            override val title: Runs,
            override val longBylineText: Runs,
            override val thumbnail: Thumbnails,
            override val lengthText: Runs,
            override val selected: Boolean,
            override val navigationEndpoint: Endpoint,
            override val videoId: String,
            override val shortBylineText: Runs,
            override val badges: List<Badge> = emptyList(),
            override val playlistSetVideoId: String,
            override val canReorder: Boolean,
            override val queueNavigationEndpoint: Endpoint?
        ): PlaylistPanelRenderer.Content.VideoRenderer
    }
}