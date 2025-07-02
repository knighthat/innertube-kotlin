package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Thumbnails

@Serializable
data class ThumbnailsImpl(
    override val thumbnails: List<ThumbnailImpl> = emptyList()
): Thumbnails {

    @Serializable
    data class ThumbnailImpl(
        override val url: String,
        override val width: Short,
        override val height: Short
    ): Thumbnails.Thumbnail
}