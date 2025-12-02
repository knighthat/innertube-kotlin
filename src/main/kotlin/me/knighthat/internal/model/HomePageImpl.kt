package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.HomePage
import me.knighthat.innertube.model.Section
import me.knighthat.innertube.response.BrowseResponse
import me.knighthat.innertube.response.Continuation
import me.knighthat.innertube.response.SectionListRenderer
import me.knighthat.innertube.response.Thumbnails


@Serializable
data class HomePageImpl(
    override val sections: List<Section>,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val continuations: List<Continuation>,
    override val visitorData: String?
): HomePage {

    companion object {

        fun from( browseResponse: BrowseResponse ): HomePage {
            val visitorData = browseResponse.responseContext.visitorData
            val thumbnails = browseResponse.background.toThumbnailList()

            val renderer = requireNotNull(
                browseResponse.contents
                    ?.singleColumnBrowseResultsRenderer
                    ?.tabs
                    ?.firstOrNull()
                    ?.tabRenderer
                    ?.content
                    ?.sectionListRenderer
            ) { "BrowseResponse doesn't contain content" }
            val sections: List<Section> = renderer.contents
                                                  .mapNotNull( SectionListRenderer.Content::musicCarouselShelfRenderer )
                                                  .map( ::createModelSectionFrom )
            val continuations = renderer.continuations

            return HomePageImpl(sections, thumbnails, continuations, visitorData)
        }
    }
}