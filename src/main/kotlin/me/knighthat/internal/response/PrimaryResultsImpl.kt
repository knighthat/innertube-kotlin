package me.knighthat.internal.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.knighthat.innertube.response.PrimaryResults

@Serializable
internal data class PrimaryResultsImpl(
    override val results: ResultsImpl
): PrimaryResults {

    @Serializable
    data class ResultsImpl(
        override val contents: List<ContentImpl> = emptyList()
    ): PrimaryResults.Results {

        @Serializable
        data class ContentImpl(
            override val videoPrimaryInfoRenderer: VideoPrimaryInfoRendererImpl?,
            override val videoSecondaryInfoRenderer: VideoSecondaryInfoRendererImpl?
        ): PrimaryResults.Results.Content {

            @Serializable
            data class VideoPrimaryInfoRendererImpl(
                override val title: RunsImpl,
                override val viewCount: ViewCountImpl,
                override val dateText: SimpleTextImpl,
                override val relativeDateText: SimpleTextImpl
            ): PrimaryResults.Results.Content.VideoPrimaryInfoRenderer {

                @Serializable
                data class ViewCountImpl(
                    override val videoViewCountRenderer: RendererImpl
                ): PrimaryResults.Results.Content.VideoPrimaryInfoRenderer.ViewCount {

                    @Serializable
                    data class RendererImpl(
                        override val viewCount: SimpleTextImpl,
                        override val shortViewCount: SimpleTextImpl,
                        override val originalViewCount: String?
                    ): PrimaryResults.Results.Content.VideoPrimaryInfoRenderer.ViewCount.Renderer
                }
            }

            @Serializable
            data class VideoSecondaryInfoRendererImpl(
                override val owner: OwnerImpl,
                override val metadataRowContainer: MetadataRowContainerImpl?,
                override val showMoreText: SimpleTextImpl?,
                override val showLessText: SimpleTextImpl?,
                override val defaultExpanded: Boolean?,
                override val descriptionCollapsedLines: Int?,
                override val attributedDescription: AttributedDescriptionImpl
            ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer {

                @Serializable
                data class OwnerImpl(
                    override val videoOwnerRenderer: RendererImpl
                ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.Owner {

                    @Serializable
                    data class RendererImpl(
                        override val thumbnail: ThumbnailsImpl,
                        override val title: RunsImpl,
                        override val navigationEndpoint: EndpointImpl,
                        override val subscriberCountText: SimpleTextImpl?,
                        override val badges: List<BadgeImpl> = emptyList()
                    ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.Owner.Renderer
                }

                @Serializable
                data class MetadataRowContainerImpl(
                    override val metadataRowContainerRenderer: RendererImpl
                ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.MetadataRowContainer {

                    @Serializable
                    data class RendererImpl(
                        override val collapsedItemCount: Int
                    ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.MetadataRowContainer.Renderer
                }

                @Serializable
                data class AttributedDescriptionImpl(
                    override val content: String,
                    override val styleRuns: List<StyleRunImpl> = emptyList(),
                    override val headerRuns: List<HeaderRunImpl> = emptyList()
                ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.AttributedDescription {

                    @Serializable
                    data class StyleRunImpl(
                        override val startIndex: UShort,
                        override val length: UShort,
                        override val styleRunExtensions: ExtensionImpl,
                        override val fontFamilyName: String
                    ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.AttributedDescription.StyleRun {

                        @Serializable
                        data class ExtensionImpl(
                            override val styleRunColorMapExtension: MapExtensionImpl
                        ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.AttributedDescription.StyleRun.Extension {

                            @Serializable
                            data class MapExtensionImpl(
                                @SerialName("colorMap") val params: List<Param> = emptyList()
                            ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.AttributedDescription.StyleRun.Extension.MapExtension {

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
                    ): PrimaryResults.Results.Content.VideoSecondaryInfoRenderer.AttributedDescription.HeaderRun
                }
            }
        }
    }
}