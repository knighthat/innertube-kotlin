package me.knighthat.internal.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.knighthat.innertube.response.PrimaryResults
import me.knighthat.innertube.response.SimpleText

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
                override val title: RunsImpl,
                override val viewCount: ViewCountImpl,
                override val dateText: SimpleTextImpl,
                override val relativeDateText: SimpleTextImpl
            ): PrimaryResults.Results.Contents.VideoPrimaryInfoRenderer {

                @Serializable
                data class ViewCountImpl(
                    override val videoViewCountRenderer: RendererImpl
                ): PrimaryResults.Results.Contents.VideoPrimaryInfoRenderer.ViewCount {

                    @Serializable
                    data class RendererImpl(
                        override val viewCount: SimpleText,
                        override val shortViewCount: SimpleText,
                        override val originalViewCount: String?
                    ): PrimaryResults.Results.Contents.VideoPrimaryInfoRenderer.ViewCount.Renderer
                }
            }

            @Serializable
            data class VideoSecondaryInfoRendererImpl(
                override val owner: OwnerImpl,
                override val metadataRowContainer: MetadataRowContainerImpl?,
                override val showMoreText: SimpleText?,
                override val showLessText: SimpleText?,
                override val defaultExpanded: Boolean?,
                override val descriptionCollapsedLines: Int?,
                override val attributedDescription: AttributedDescriptionImpl
            ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer {

                @Serializable
                data class OwnerImpl(
                    override val videoOwnerRenderer: RendererImpl
                ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.Owner {

                    @Serializable
                    data class RendererImpl(
                        override val thumbnail: ThumbnailsImpl,
                        override val title: RunsImpl,
                        override val navigationEndpoint: EndpointImpl,
                        override val subscriberCountText: SimpleTextImpl?,
                        override val badges: List<BadgeImpl> = emptyList()
                    ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.Owner.Renderer
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
                    override val content: String,
                    override val styleRuns: List<StyleRunImpl> = emptyList(),
                    override val headerRuns: List<HeaderRunImpl> = emptyList()
                ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.AttributedDescription {

                    @Serializable
                    data class StyleRunImpl(
                        override val startIndex: UShort,
                        override val length: UShort,
                        override val styleRunExtensions: ExtensionImpl,
                        override val fontFamilyName: String
                    ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.AttributedDescription.StyleRun {

                        @Serializable
                        data class ExtensionImpl(
                            override val styleRunColorMapExtension: MapExtensionImpl
                        ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.AttributedDescription.StyleRun.Extension {

                            @Serializable
                            data class MapExtensionImpl(
                                @SerialName("colorMap") val params: List<Param> = emptyList()
                            ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.AttributedDescription.StyleRun.Extension.MapExtension {

                                @Transient
                                override val colorMap: Map<String, Long> = params.associate { it.key to it.value }

                                @Serializable
                                data class Param(val key: String, val value: Long)
                            }
                        }
                    }

                    @Serializable
                    data class HeaderRunImpl(
                        override val startIndex: UShort,
                        override val length: UShort,
                        override val headerMapping: String
                    ): PrimaryResults.Results.Contents.VideoSecondaryInfoRenderer.AttributedDescription.HeaderRun
                }
            }
        }
    }
}