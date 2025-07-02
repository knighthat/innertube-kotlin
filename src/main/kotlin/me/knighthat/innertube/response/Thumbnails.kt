package me.knighthat.innertube.response


interface Thumbnails {

    val thumbnails: List<Thumbnail>

    interface Thumbnail {

        val url: String
        val width: Short
        val height: Short
    }
}