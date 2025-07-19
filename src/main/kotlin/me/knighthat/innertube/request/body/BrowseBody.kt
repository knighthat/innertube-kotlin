package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable
import me.knighthat.innertube.request.body.browse.TypeBuilder

@Serializable
data class BrowseBody(
    val browseId: String?,
    val params: String?,
    val continuation: String?,
    val formData: FormData?,
    override val context: Context
): RequestBody {

    companion object {
        @JvmStatic
        fun builder( context: Context ): TypeBuilder = BrowseBodyBuilder(context)
    }
}