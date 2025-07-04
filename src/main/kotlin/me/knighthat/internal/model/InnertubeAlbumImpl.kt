package me.knighthat.internal.model

import kotlinx.serialization.Serializable
import me.knighthat.innertube.model.InnertubeAlbum
import me.knighthat.innertube.response.Endpoint
import me.knighthat.innertube.response.Thumbnails

@Serializable
internal data class InnertubeAlbumImpl(
    override val id: String,
    override val name: String,
    override val thumbnails: List<Thumbnails.Thumbnail>,
    override val isExplicit: Boolean,
    override val artists: List<Endpoint.Browse>,
    override val year: String
): InnertubeAlbum
