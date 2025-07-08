package me.knighthat.innertube.response


interface MusicShelfRenderer {

    val title: Runs?
    val contents: List<Content>
    val bottomText: Runs?
    val bottomEndpoint: Endpoint?
    val contentsMultiSelectable: Boolean?

    interface Content {

        val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer
    }
}