package me.knighthat.innertube.model

import me.knighthat.innertube.Constants
import org.intellij.lang.annotations.MagicConstant

fun interface AccessibleViaUrl {

    fun shareUrl(
        @MagicConstant(stringValues = [Constants.YOUTUBE_URL, Constants.YOUTUBE_MUSIC_URL])
        host: String
    ): String
}