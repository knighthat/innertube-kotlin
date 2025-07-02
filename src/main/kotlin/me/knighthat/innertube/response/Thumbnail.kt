package me.knighthat.innertube.response


interface Thumbnail {

    val musicThumbnailRenderer: Renderer

    interface Renderer {

        val thumbnail: Thumbnails
        val thumbnailCrop: String?
        val thumbnailScale: String?
    }
}