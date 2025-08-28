package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable
import me.knighthat.innertube.request.body.player.Builder

@Serializable
data class PlayerBody(
    val videoId: String,
    val params: String?,
    val racyCheckOk: Boolean?,
    val contentCheckOk: Boolean?,
    val serviceIntegrityDimensions: ServiceIntegrityDimensions?,
    val contentPlaybackContext: ContentPlaybackContext?,
    override val context: Context
): RequestBody {

    companion object {

        @JvmStatic
        fun builder( context: Context ): Builder = PlayerBodyBuilder(context)
    }

    @Serializable
    data class ServiceIntegrityDimensions( val poToken: String )

    @Serializable
    data class ContentPlaybackContext(
        val html5Preference: String,
        val signatureTimestamp: Int
    )
}