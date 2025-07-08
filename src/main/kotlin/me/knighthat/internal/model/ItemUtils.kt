package me.knighthat.internal.model

import me.knighthat.innertube.Constants
import me.knighthat.innertube.PageType
import me.knighthat.innertube.response.Badge
import me.knighthat.innertube.response.Endpoint
import me.knighthat.innertube.response.Runs
import me.knighthat.innertube.response.Thumbnail
import me.knighthat.innertube.response.Thumbnails

internal data class AlbumAndArtists(
    val album: Runs.Run?,
    val artists: List<Runs.Run>
)

internal val Runs.firstText: String
    get() = runs.firstOrNull()?.text.orEmpty()

/**
 * Extract album and artists.
 *
 * Album or artist is filtered by their respected [PageType].
 *
 * Album will always located at the first element of the pair - [Pair.first]
 *
 * Artists list will never be null (nor contain null elements) only empty
 * when there's no artist found
 *
 * @return album's and artists' browse endpoints in 1 list
 */
internal fun Runs.extractArtistAndAlbum(): AlbumAndArtists {
    val artists = mutableListOf<Runs.Run>()
    var album: Runs.Run? = null

    for( run in runs )
        when( run.navigationEndpoint.pageType ) {
            PageType.ALBUM  -> album = run
            PageType.ARTIST -> artists.add( run )
            else            -> continue
        }

    return AlbumAndArtists(album, artists)
}

internal val List<Badge>.containsExplicitBadge: Boolean
    get() = any { it.musicInlineBadgeRenderer?.icon?.iconType == Badge.EXPLICIT }

internal fun Thumbnail?.toThumbnailList(): List<Thumbnails.Thumbnail> =
    this?.musicThumbnailRenderer?.thumbnail?.thumbnails.orEmpty()

internal val Endpoint?.pageType: String?
    get() = this?.browseEndpoint
                ?.browseEndpointContextSupportedConfigs
                ?.browseEndpointContextMusicConfig
                ?.pageType

internal val String.isYouTubeHost: Boolean
    get() = this == Constants.YOUTUBE_URL || this == Constants.YOUTUBE_MUSIC_URL