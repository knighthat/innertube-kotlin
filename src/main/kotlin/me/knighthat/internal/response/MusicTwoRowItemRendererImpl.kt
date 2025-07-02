package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.MusicTwoRowItemRenderer

@Serializable
internal data class MusicTwoRowItemRendererImpl(
    override val thumbnailRenderer: ThumbnailImpl,
    override val aspectRatio: String,
    override val title: RunsImpl,
    override val subtitle: RunsImpl,
    override val navigationEndpoint: EndpointImpl,
    override val thumbnailOverlay: OverlayImpl?,
    override val subtitleBadges: List<BadgeImpl> = emptyList()
): MusicTwoRowItemRenderer