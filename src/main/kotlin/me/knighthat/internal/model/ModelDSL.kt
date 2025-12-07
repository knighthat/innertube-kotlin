package me.knighthat.internal.model

import me.knighthat.innertube.PageType
import me.knighthat.innertube.model.InnertubeItem
import me.knighthat.innertube.model.Section
import me.knighthat.innertube.response.MusicCarouselShelfRenderer
import me.knighthat.innertube.response.MusicResponsiveListItemRenderer
import me.knighthat.innertube.response.MusicShelfRenderer


internal fun createModelSectionFrom( renderer: MusicCarouselShelfRenderer ): Section {
    val header = renderer.header.musicCarouselShelfBasicHeaderRenderer
    val title = header.title.runs.firstOrNull()
    val browseEndpoint = title?.navigationEndpoint?.browseEndpoint

    val content = mutableListOf<InnertubeItem>()
    for( item in renderer.contents ) {
        val mrlir = item.musicResponsiveListItemRenderer
        if( mrlir != null ) {
            val song = InnertubeSongImpl.from( mrlir )
            content.add( song )

            continue
        }

        val mtwir = item.musicTwoRowItemRenderer ?: continue
        val item: InnertubeItem = when( mtwir.navigationEndpoint.pageType ) {
            PageType.ARTIST     -> InnertubeArtistImpl.from( mtwir )
            PageType.ALBUM      -> InnertubeAlbumImpl.from( mtwir )
            PageType.PLAYLIST   -> InnertubePlaylistImpl.from( mtwir )
            // Ignore items with unknown page type (have no parser for it)
            else                -> continue
        }
        content.add( item )
    }

    return object : Section {
        override val title: String? = title?.text
        override val accessibilityLabel: String? = header.accessibilityData?.accessibilityData?.label
        override val browseId: String? = browseEndpoint?.browseId
        override val params: String? = browseEndpoint?.params
        override val contents: List<InnertubeItem> = content.toList()
    }
}

internal fun createModelSectionFrom( renderer: MusicShelfRenderer ): Section {
    val content = mutableListOf<InnertubeItem>()
    for( item in renderer.contents ) {
        val mrlir = item.musicResponsiveListItemRenderer ?: continue
        val song = InnertubeSongImpl.from( mrlir )
        content.add( song )
    }
    val browseEndpoint = renderer.bottomEndpoint?.browseEndpoint

    return object : Section {
        override val title: String? = renderer.title?.firstText
        override val accessibilityLabel: String? = null
        override val browseId: String? = browseEndpoint?.browseId
        override val params: String? = browseEndpoint?.params
        override val contents: List<InnertubeItem> = content.toList()
    }
}

internal fun createInnertubeItemFrom( renderer: MusicResponsiveListItemRenderer ): InnertubeItem? {
    if( renderer.navigationEndpoint?.watchEndpoint != null
        || renderer.playlistItemData?.videoId != null )
        return InnertubeSongImpl.from( renderer )

    return when( renderer.navigationEndpoint.pageType ) {
        PageType.ARTIST     -> InnertubeArtistImpl.from( renderer )
        PageType.ALBUM      -> InnertubeAlbumImpl.from( renderer )
        PageType.PLAYLIST   -> InnertubePlaylistImpl.from( renderer )
        // Ignore items with unknown page type (have no parser for it)
        else                -> null
    }
}