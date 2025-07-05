package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.response.Thumbnails

@Serializable
internal data class InnertubeArtistImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val description: String?,
    override val shortNumSubscribers: String?,
    override val longNumSubscribers: String?,
    override val shortNumMonthlyAudience: String?,
    override val sections: List<InnertubeArtist.Section>
): InnertubeArtist {
    @Serializable
    internal data class SectionImpl(
        override val title: String,
        override val browseId: String?,
        override val params: String?,
        override val contents: List<InnertubeItem>
    ): InnertubeArtist.Section
}
