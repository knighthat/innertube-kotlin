package me.knighthat.innertube.response


interface Overlay {

    val musicItemThumbnailOverlayRenderer: MusicItemThumbnailOverlayRenderer

    interface MusicItemThumbnailOverlayRenderer {

        val background: Background
        val contentPosition: String
        val displayStyle: String

        interface Background {

            val verticalGradient: VerticalGradient

            interface VerticalGradient {

                val gradientLayerColors: List<String>
            }
        }
    }
}