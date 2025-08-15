package me.knighthat.innertube.model

import org.intellij.lang.annotations.MagicConstant

interface InnertubeRankedArtist: InnertubeArtist {

    companion object {

        const val RANK_UP_ICON = "ARROW_DROP_UP"
        const val RANK_DOWN_ICON = "ARROW_DROP_DOWN"
        const val RANK_NEUTRAL = "ARROW_CHART_NEUTRAL"
    }

    val rank: String

    @get:MagicConstant(stringValues = [RANK_UP_ICON, RANK_DOWN_ICON, RANK_NEUTRAL])
    val iconType: String?
}