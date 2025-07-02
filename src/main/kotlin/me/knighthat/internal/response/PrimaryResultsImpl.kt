package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.*

@Serializable
internal data class PrimaryResultsImpl(
    override val results: ResultsImpl
): PrimaryResults {

    @Serializable
    data class ResultsImpl(
        override val contents: ContentsImpl
    ): PrimaryResults.Results {

        @Serializable
        data class ContentsImpl(
            override val videoPrimaryInfoRenderer: VideoPrimaryInfoRendererImpl?,
            override val videoSecondaryInfoRenderer: VideoSecondaryInfoRendererImpl?
        ): PrimaryResults.Results.Contents {

            @Serializable
            data class VideoPrimaryInfoRendererImpl(
                override val title: RunsImpl?,
                override val viewCount: ViewCountImpl?,
                override val dateText: SimpleTextImpl?,
                override val relativeDateText: SimpleTextImpl?
            ): PrimaryResults.Results.Contents.VideoPrimaryInfoRenderer {

                @Serializable
                data class ViewCountImpl(
                    override val videoViewCountRenderer: VideoViewCountRendererImpl
                ): PrimaryResults.Results.Contents.VideoPrimaryInfoRenderer.ViewCount {

                    @Serializable
                    data class VideoViewCountRendererImpl(
                        override val viewCount: SimpleText?,
                        override val shortViewCount: SimpleText?,
                        override val originalViewCount: String?
                    ): PrimaryResults.Results.Contents.VideoPrimaryInfoRenderer.ViewCount.VideoViewCountRenderer
                }
            }

            @Serializable
            data class VideoSecondaryInfoRendererImpl(
                override val owner: OwnerImpl?,
                override val metadataRowContainer: MetadataRowContainerImpl?,
                override val showMoreText: SimpleText?,
                override val showLessText: SimpleText?,
                override val defaultExpanded: Boolean?,
                override val descriptionCollapsedLines: Int?,
                override val attributedDescription: AttributedDescriptionImpl?
            ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer {

                @Serializable
                data class OwnerImpl(
                    override val videoOwnerRenderer: Thumbnail.Renderer?,
                    override val navigationEndpoint: Endpoint?,
                    override val subscriberCountText: SimpleText?,
                    override val badges: List<Badge> = emptyList()
                ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.Owner {

                    @Serializable
                    data class VideoOwnerRendererImpl(
                        override val thumbnail: Thumbnails?,
                        override val title: Runs?
                    ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.Owner.VideoOwnerRenderer
                }

                @Serializable
                data class MetadataRowContainerImpl(
                    override val metadataRowContainerRenderer: RendererImpl
                ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.MetadataRowContainer {

                    @Serializable
                    data class RendererImpl(
                        override val collapsedItemCount: Int
                    ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.MetadataRowContainer.Renderer
                }

                @Serializable
                data class AttributedDescriptionImpl(
                    override val content: String?
                ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.AttributedDescription
            }
        }
    }
}