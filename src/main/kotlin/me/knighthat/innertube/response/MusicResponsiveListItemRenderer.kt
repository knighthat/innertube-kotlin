package me.knighthat.innertube.response


interface MusicResponsiveListItemRenderer {

    val thumbnail: Thumbnail?
    val overlay: Overlay?
    val flexColumns: List<Colum>
    val fixedColumns: List<Colum>
    val badges: List<Badge>
    val playlistItemData: PlaylistItemData?
    val flexColumnDisplayStyle: String?
    val navigationEndpoint: Endpoint?
    val itemHeight: String?
    val index: Runs?

    interface Colum {

        val musicResponsiveListItemFlexColumnRenderer: Renderer?
        val musicResponsiveListItemFixedColumnRenderer: Renderer?

        interface Renderer {

            val text: Runs?
            val displayPriority: String
            val size: String?
        }
    }

    interface PlaylistItemData {

        val playlistSetVideoId: String?
        val videoId: String?
    }
}