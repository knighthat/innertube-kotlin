package me.knighthat.innertube.model

import me.knighthat.innertube.response.Thumbnails

interface AccountInfo {

    val name: String
    val email: String?
    val channelHandle: String?
    val thumbnailUrl: List<Thumbnails.Thumbnail>
}