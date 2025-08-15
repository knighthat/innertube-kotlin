package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Microformat

@Serializable
data class MicroformatImpl(
    override val microformatDataRenderer: RendererImpl
): Microformat {

    @Serializable
    data class RendererImpl(
        override val urlCanonical: String?,
        override val title: String?,
        override val description: String?,
        override val thumbnail: ThumbnailsImpl?,
        override val siteName: String?,
        override val appName: String?,
        override val ogType: String?,
        override val twitterCardType: String?,
        override val twitterSiteHandle: String?,
        override val schemaDotOrgType: String?,
        override val noindex: Boolean?,
        override val unlisted: Boolean?,
        override val paid: Boolean?,
        override val familySafe: Boolean?,
        override val tags: List<String> = emptyList(),
        override val availableCountries: List<String> = emptyList(),
        override val pageOwnerDetails: PageOwnerDetailsImpl?,
        override val videoDetails: VideoDetailsImpl?,
        override val viewCount: String?,
        override val publishDate: String?,
        override val category: String?,
        override val uploadDate: String?
    ): Microformat.Renderer {

        @Serializable
        data class PageOwnerDetailsImpl(
            override val name: String,
            override val externalChannelId: String,
            override val youtubeProfileUrl: String
        ): Microformat.Renderer.PageOwnerDetails

        @Serializable
        data class VideoDetailsImpl(
            override val externalVideoId: String,
            override val durationSeconds: String,
            override val durationIso8601: String
        ): Microformat.Renderer.VideoDetails
    }
}
