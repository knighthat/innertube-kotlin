package me.knighthat.innertube.util


/**
 * A set of helpful methods to assist with
 * Innertube requests
 */
object InnertubeUtils {

    /**
     * Generate a complete random strings with [length] length.
     */
    fun randomString( length: Int ): String {
        val allowedCharset: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return String(CharArray(length) { allowedCharset.random() })
    }
}