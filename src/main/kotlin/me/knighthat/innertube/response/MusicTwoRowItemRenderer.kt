package me.knighthat.innertube.response


interface MusicTwoRowItemRenderer {

    val thumbnailRenderer: Thumbnail
    val aspectRatio: String
    val title: Runs
    val subtitle: Runs
    val navigationEndpoint: Endpoint
    val thumbnailOverlay: Overlay?
    val subtitleBadges: List<Badge>
}