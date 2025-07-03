package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable
import me.knighthat.innertube.request.body.next.Builder

@Serializable
data class NextBody(
    val videoId: String?,
    val playlistId: String?,
    val params: String?,
    override val context: Context
): RequestBody {

    companion object {
        @JvmStatic
        fun builder( context: Context ): Builder = NextBodyBuilder( context )
    }
}
