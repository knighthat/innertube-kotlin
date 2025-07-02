package me.knighthat.innertube.response


interface PrimaryResults {

    val results: Results

    interface Results {

        val contents: Contents

        interface Contents {

            val videoPrimaryInfoRenderer: VideoPrimaryInfoRenderer?
            val videoSecondaryInfoRenderer: VideoSecondaryInfoRenderer?

            interface VideoPrimaryInfoRenderer {

                val title: Runs?
                val viewCount: ViewCount?
                val dateText: SimpleText?
                val relativeDateText: SimpleText?

                interface ViewCount {

                    val videoViewCountRenderer: VideoViewCountRenderer

                    interface VideoViewCountRenderer {

                        val viewCount: SimpleText?
                        val shortViewCount: SimpleText?
                        val originalViewCount: String?
                    }
                }
            }

            interface VideoSecondaryInfoRenderer {

                val owner: Owner?
                val metadataRowContainer: MetadataRowContainer?
                val showMoreText: SimpleText?
                val showLessText: SimpleText?
                val defaultExpanded: Boolean?
                val descriptionCollapsedLines: Int?
                val attributedDescription: AttributedDescription?

                interface Owner {

                    val videoOwnerRenderer: Thumbnail.Renderer?
                    val navigationEndpoint: Endpoint?
                    val subscriberCountText: SimpleText?
                    val badges: List<Badge>

                    interface VideoOwnerRenderer {

                        val thumbnail: Thumbnails?
                        val title: Runs?
                    }
                }

                interface MetadataRowContainer {

                    val metadataRowContainerRenderer: Renderer

                    interface Renderer {

                        val collapsedItemCount: Int
                    }
                }

                interface AttributedDescription {

                    val content: String?
                }
            }
        }
    }
}