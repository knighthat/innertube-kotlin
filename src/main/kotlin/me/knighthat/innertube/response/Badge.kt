package me.knighthat.innertube.response


interface Badge {

    companion object {
        const val EXPLICIT: String = "MUSIC_EXPLICIT_BADGE"
    }

    val musicInlineBadgeRenderer: Renderer?
    val metadataBadgeRenderer: Renderer?

    interface Renderer {

        val style: String?
        val tooltip: String?
        val accessibilityData: Accessibility?
        val icon: Icon?
    }
}