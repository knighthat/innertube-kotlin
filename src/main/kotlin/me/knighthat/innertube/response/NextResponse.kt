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

        val playerOverlayRenderer: PlayerOverlayRenderer

        interface PlayerOverlayRenderer {

            // Missing "actions"
            val browserMediaSession: BrowserMediaSession

            interface BrowserMediaSession {

                val browserMediaSessionRenderer: BrowserMediaSessionRenderer

                interface BrowserMediaSessionRenderer {

                    val album: Runs?
                    val thumbnailDetails: Thumbnails
                }
            }
        }
    }
}