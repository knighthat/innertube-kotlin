package me.knighthat.internal.response

import kotlinx.serialization.Serializable
import me.knighthat.innertube.response.Continuation

@Serializable
data class ContinuationImpl(
    override val nextContinuationData: DataImpl?,
    override val nextRadioContinuationData: DataImpl?
): Continuation {

    @Serializable
    data class DataImpl(
        override val continuation: String
    ): Continuation.Data
}
