package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.PageType
import me.knighthat.innertube.model.InnertubeCharts
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.response.MusicCarouselShelfRenderer
import me.knighthat.innertube.response.MusicMultiSelectMenuRenderer
import me.knighthat.innertube.response.MusicShelfRenderer
import me.knighthat.innertube.response.SectionListRenderer
import java.util.Base64

@Serializable
internal data class InnertubeChartsImpl(
    override val selectedCountryName: String,
    override val menu: InnertubeCharts.Menu,
    override val sections: List<InnertubeCharts.Section>
): InnertubeCharts {

    companion object {

        fun from( renderer: SectionListRenderer ): InnertubeCharts {
            var selected: String? = null
            var menu: InnertubeCharts.Menu? = null
            val sections = mutableListOf<InnertubeCharts.Section>()

            renderer.contents.forEach { content ->
                content.musicShelfRenderer
                       ?.subheaders
                       ?.firstNotNullOfOrNull(MusicShelfRenderer.Subheader::musicSideAlignedItemRenderer )
                       ?.startItems
                       ?.firstNotNullOfOrNull( MusicShelfRenderer.Subheader.Renderer.Item::musicSortFilterButtonRenderer )
                       ?.also { selected = it.title.firstText }
                       ?.menu
                       ?.musicMultiSelectMenuRenderer
                       ?.let(MenuImpl::from )
                       ?.also { menu = it }

                content.musicCarouselShelfRenderer
                       ?.let(SectionImpl::from )
                       ?.also( sections::add )
            }

            return InnertubeChartsImpl(
                requireNotNull( selected ),
                requireNotNull( menu ),
                sections
            )
        }
    }

    @Serializable
    internal data class MenuImpl(
        override val title: String,
        override val items: List<InnertubeCharts.Menu.Item>
    ) : InnertubeCharts.Menu {

        companion object {

            fun from( renderer: MusicMultiSelectMenuRenderer ): InnertubeCharts.Menu =
                MenuImpl(
                    title = renderer.title.musicMenuTitleRenderer.primaryText.firstText,
                    items = renderer.options
                                    .mapNotNull( MusicMultiSelectMenuRenderer.Option::musicMultiSelectMenuItemRenderer )
                                    .map( ItemImpl::from )
                )
        }

        @Serializable
        internal data class ItemImpl(
            override val countryDisplayName: String,
            override val countryCode: String
        ) : InnertubeCharts.Menu.Item {

            companion object {

                private val REGION_CODE_REGEX = Regex("^EidleHBsb3JlX2NoYXJ0c19jb3VudHJ5X21lbnVfMzE2NzY2NTY3([a-zA-Z0-9]{2,3})gkQEoAQ%3D%3D$")

                fun from( renderer: MusicMultiSelectMenuRenderer.Option.ItemRenderer ): InnertubeCharts.Menu.Item {
                    // No option to fail
                    val encodedKey = REGION_CODE_REGEX.find( renderer.formItemEntityKey )!!.groupValues[1]
                    // This capture group usually contains 3 letters, but base64 requires modulo of 4
                    // so this step is added to pad the missing letter(s)
                    val padding = when ( encodedKey.length % 4 ) {
                        0 -> ""
                        2 -> "=="
                        3 -> "="
                        1 -> {
                            // If length % 4 is 1, it's an invalid unpadded Base64 string length
                            // This case should ideally not happen if the unpadded string was correctly formed
                            // from binary data, as it implies an incomplete 6-bit block.
                            // A decoder expecting padding might throw an error anyway.
                            // Forcing padding here might lead to incorrect decoding if the original data was truly malformed.
                            throw IllegalArgumentException("Invalid Base64 string length for padding: ${encodedKey.length}")
                        }
                        else -> "" // Should not happen
                    }
                    val decodedKeyRaw = Base64.getDecoder().decode( encodedKey + padding )
                    val decodedKey = String(decodedKeyRaw, Charsets.UTF_8)

                    return ItemImpl(
                        renderer.title.firstText,
                        decodedKey
                    )
                }
            }
        }
    }

    @Serializable
    internal data class SectionImpl(
        override val title: String,
        override val contents: List<InnertubeItem>
    ) : InnertubeCharts.Section {

        companion object {

            fun from( renderer: MusicCarouselShelfRenderer ): InnertubeCharts.Section? {
                val contents = mutableListOf<InnertubeItem>()
                renderer.contents.forEach { content ->
                    content.musicResponsiveListItemRenderer
                           ?.let( InnertubeRankedArtistImpl::from )
                           ?.also( contents::add )

                    content.musicTwoRowItemRenderer
                            ?.let { itemRenderer ->
                                var result: InnertubeItem? = null

                                itemRenderer.navigationEndpoint
                                            .browseEndpoint
                                            ?.browseEndpointContextSupportedConfigs
                                            ?.browseEndpointContextMusicConfig
                                            ?.pageType
                                            ?.also { pageType ->
                                                result = when( pageType ) {
                                                    PageType.ARTIST     -> InnertubeArtistImpl.Companion.from(
                                                        itemRenderer
                                                    )
                                                    PageType.ALBUM      -> InnertubeAlbumImpl.from( itemRenderer )
                                                    PageType.PLAYLIST   -> InnertubePlaylistImpl.from( itemRenderer )
                                                    else                -> null
                                                }
                                            }

                                itemRenderer.navigationEndpoint
                                            .watchEndpoint
                                            ?.also {
                                                result = InnertubeSongImpl.from( itemRenderer )
                                            }

                                result
                            }
                            ?.also( contents::add )
                }

                return SectionImpl(
                    renderer.header.musicCarouselShelfBasicHeaderRenderer.title.firstText,
                    contents
                )
            }
        }
    }
}
