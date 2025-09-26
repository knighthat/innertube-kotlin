package me.knighthat.innertube.response


interface NextResponse : InnertubeResponse {

    val contents: Contents
    val currentVideoEndpoint: Endpoint
    val playerOverlays: PlayerOverlays

    interface Contents {

        val singleColumnMusicWatchNextResultsRenderer: SingleColumnMusicWatchNextResultsRenderer?
        val twoColumnWatchNextResults: TwoColumnWatchNextResults?

        interface SingleColumnMusicWatchNextResultsRenderer {

            val tabbedRenderer: TabbedRenderer

            interface TabbedRenderer {

                val watchNextTabbedResultsRenderer: Tabs
            }
        }

        interface TwoColumnWatchNextResults {

            val results: PrimaryResults
        }
    }

    interface PlayerOverlays {

        val playerOverlayRenderer: Renderer

        interface Renderer {

            // Missing actions, endScreen, autoplay, shareButton, addToMenu, videoDetails, autonavToggle, decoratedPlayerBarRenderer, speedmasterUserEdu
            val browserMediaSession: BrowserMediaSession?

            interface BrowserMediaSession {

                val browserMediaSessionRenderer: Renderer

                interface Renderer {

                    val album: Runs?
                    val thumbnailDetails: Thumbnails
                }
            }
        }
    }
}