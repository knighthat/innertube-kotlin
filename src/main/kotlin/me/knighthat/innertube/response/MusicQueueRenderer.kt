package me.knighthat.innertube.response

interface MusicQueueRenderer {

    val content: Content

    interface Content {

        val playlistPanelRenderer: PlaylistPanelRenderer
    }
}