package me.knighthat.innertube.response


interface SectionListRenderer {

    val contents: List<Content>
    val continuations: List<Continuation>
    val header: Header?

    interface Content {

        val musicDescriptionShelfRenderer: MusicDescriptionShelfRenderer?
        val musicTastebuilderShelfRenderer: MusicTastebuilderShelfRenderer?
        val musicResponsiveHeaderRenderer: MusicResponsiveHeaderRenderer?
        val musicEditablePlaylistDetailHeaderRenderer: MusicEditablePlaylistDetailHeaderRenderer?
        val musicShelfRenderer: MusicShelfRenderer?
        val musicCarouselShelfRenderer: MusicCarouselShelfRenderer?
        val gridRenderer: GridRenderer?
        val musicPlaylistShelfRenderer: MusicPlaylistShelfRenderer?
        val musicCardShelfRenderer: MusicCardShelfRenderer?

        interface MusicDescriptionShelfRenderer {

            val header: Runs?
            val subheader: Runs?
            val description: Runs
            val shelfStyle: String?
            val maxCollapsedLines: Int?
            val maxExpandedLines: Int?
            val footer: Runs?
        }

        interface MusicTastebuilderShelfRenderer {

            val thumbnail: Thumbnail
            val primaryText: Runs
            val secondaryText: Runs
            val isVisible: Boolean?

            interface Thumbnail {

                val musicTastebuilderShelfThumbnailRenderer: me.knighthat.innertube.response.Thumbnail.Renderer
            }
        }

        interface MusicResponsiveHeaderRenderer {

            val thumbnail: Thumbnail
            val title: Runs
            val subtitle: Runs
            val description: Description?
            val straplineTextOne: Runs?
            val straplineThumbnail: Thumbnail?
            val subtitleBadge: List<Badge>
            val secondSubtitle: Runs?

            interface Description {

                val musicDescriptionShelfRenderer: MusicDescriptionShelfRenderer
            }
        }


        interface MusicEditablePlaylistDetailHeaderRenderer {
            val header: Header

            interface Header {

                val musicResponsiveHeaderRenderer: MusicResponsiveHeaderRenderer
            }
        }

        interface GridRenderer {

            val items: List<Item>

            interface Item {

                val musicTwoRowItemRenderer: MusicTwoRowItemRenderer
            }
        }
    }

    interface Header {

        val chipCloudRenderer: ChipCloudRenderer
    }
}