package me.knighthat.innertube.response


interface BrowseResponse : InnertubeResponse {

    val contents: Contents
    val header: Header?
    val maxAgeStoreSeconds: Int?
    val microformat: Microformat?
    val background: Thumbnail?
    val onResponseReceivedActions: List<ResponseReceivedAction>

    interface Contents {

        val singleColumnBrowseResultsRenderer: Tabs?
        val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer?
        val sectionListRenderer: SectionListRenderer?

        interface TwoColumnBrowseResultsRenderer {

            val tabs: List<Tabs.Tab>
            val secondaryContents: SecondaryContents?

            interface SecondaryContents {

                val sectionListRenderer: SectionListRenderer
            }
        }
    }

    interface Header {

        val musicImmersiveHeaderRenderer: MusicImmersiveHeaderRenderer?
        val musicHeaderRenderer: MusicHeaderRenderer?

        interface MusicImmersiveHeaderRenderer {

            val title: Runs
            val description: Runs
            val thumbnail: Thumbnail
            val shareEndpoint: Endpoint?
            val monthlyListenerCount: Runs
        }

        interface MusicHeaderRenderer {

            val title: Runs
        }
    }

    interface ResponseReceivedAction {

        val appendContinuationItemsAction: AppendContinuationItemsAction

        interface AppendContinuationItemsAction {

            val continuationItems: List<MusicPlaylistShelfRenderer.Content>
        }
    }
}