package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.InnertubeResponse

@Serializable
data class InnertubeResponseImpl(
    override val responseContext: ContextImpl
): InnertubeResponse {

    @Serializable
    data class ContextImpl(
        override val visitorData: String?
    ): InnertubeResponse.Context
}