package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.ActiveAccountHeaderRenderer

@Serializable
internal data class ActiveAccountHeaderRendererImpl(
    override val accountName: RunsImpl,
    override val email: RunsImpl?,
    override val channelHandle: RunsImpl?,
    override val accountPhoto: ThumbnailsImpl
): ActiveAccountHeaderRenderer
