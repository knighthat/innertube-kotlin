package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Thumbnail

@Serializable
internal data class ThumbnailImpl(
    override val musicThumbnailRenderer: RendererImpl
): Thumbnail {

    @Serializable
    internal data class RendererImpl(
        override val thumbnail: ThumbnailsImpl,
        override val thumbnailCrop: String?,
        override val thumbnailScale: String?
    ): Thumbnail.Renderer
}