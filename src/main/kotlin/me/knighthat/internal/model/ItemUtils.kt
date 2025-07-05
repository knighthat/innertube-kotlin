package me.knighthat.internal.model

import me.knighthat.innertube.PageType
import me.knighthat.innertube.response.Badge
import me.knighthat.innertube.response.Endpoint
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.Thumbnail
import me.knighthat.innertube.response.Thumbnails
import java.util.LinkedList

internal val Runs.firstText: String
    get() = runs.firstOrNull()?.text.orEmpty()

/**
 * Extract album and artists.
 *
 * Album or artist is filtered by their respected [PageType].
 *
 * Album will always located at the beginning of the list,
 * if album browse endpoint doesn't exist in [Runs], then
 * list will be appended with a `null`.
 *
 * If the list only contains 1 `null` element, it means
 * nor album or artists found.
 *
 * @return album's and artists' browse endpoints in 1 list
 */
internal fun Runs.extractArtistAndAlbum(): LinkedHashSet<Runs.Run?> {
    val artists = mutableListOf<Runs.Run>()
    var album: Runs.Run? = null

    for( run in runs )
        when( run.pageType ) {
            PageType.ALBUM  -> album = run
            PageType.ARTIST -> artists.add( run )
            else            -> continue
        }

    return linkedSetOf( album, *artists.toTypedArray() )
}

internal val List<Badge>.containsExplicitBadge: Boolean
    get() = any { it.musicInlineBadgeRenderer?.icon?.iconType == Badge.EXPLICIT }

internal fun Thumbnail?.toThumbnailList(): List<Thumbnails.Thumbnail> =
    this?.musicThumbnailRenderer?.thumbnail?.thumbnails.orEmpty()

internal val Runs.Run.pageType: String?
    get() = navigationEndpoint?.browseEndpoint
                              ?.browseEndpointContextSupportedConfigs
                              ?.browseEndpointContextMusicConfig
                              ?.pageType