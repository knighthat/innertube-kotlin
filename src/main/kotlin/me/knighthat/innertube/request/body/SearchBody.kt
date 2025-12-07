package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable
import me.knighthat.innertube.SearchFilter
import me.knighthat.innertube.request.body.search.Builder
import org.intellij.lang.annotations.MagicConstant

@Serializable
data class SearchBody(
    val query: String?,
    @param:MagicConstant(valuesFromClass = SearchFilter::class)
    val params: String?,
    val continuation: String?,
    override val context: Context
): RequestBody {

    companion object {

        @JvmStatic
        fun builder( context: Context ): Builder = SearchBodyBuilder(context)
    }
}