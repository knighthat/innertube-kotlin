package me.knighthat.internal.model

import me.knighthat.innertube.model.InnertubeArtist
import me.knighthat.innertube.model.InnertubeSongDetails
import me.knighthat.innertube.response.PrimaryResults


internal data class InnertubeSongDetailsImpl(
    override val title: String,
    override val viewCount: InnertubeSongDetails.ViewCount,
    override val releaseDate: String,
    override val relativeReleaseDate: String,
    override val description: String,
    override val artist: InnertubeArtist
): InnertubeSongDetails {

    companion object {

        fun from( results: List<PrimaryResults.Results.Content> ): InnertubeSongDetails {
            val primaryInfo = requireNotNull(
                results.firstNotNullOfOrNull( PrimaryResults.Results.Content::videoPrimaryInfoRenderer )
            ) { "Content doesn't have videoPrimaryInfoRenderer" }
            val viewCount = object: InnertubeSongDetails.ViewCount {
                override val long: String =
                    primaryInfo.viewCount.videoViewCountRenderer.viewCount.simpleText
                override val short: String =
                    primaryInfo.viewCount.videoViewCountRenderer.shortViewCount.simpleText
            }

            val secondaryInfo = requireNotNull(
                results.firstNotNullOfOrNull( PrimaryResults.Results.Content::videoSecondaryInfoRenderer )
            ) { "Content doesn't have videoSecondaryInfoRenderer" }
            val artist = InnertubeArtistImpl.from( secondaryInfo.owner.videoOwnerRenderer )

            return InnertubeSongDetailsImpl(
                title = primaryInfo.title.firstText,
                viewCount = viewCount,
                releaseDate = primaryInfo.dateText.simpleText,
                relativeReleaseDate = primaryInfo.relativeDateText.simpleText,
                description = secondaryInfo.attributedDescription.content,
                artist = artist
            )
        }
    }

}
