package me.knighthat.innertube.response


interface MusicCardShelfRenderer {

    val thumbnail: Thumbnail
    val title: Runs
    val subtitle: Runs
    val contents: List<Content>

    interface Content {

        val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer?
    }
}