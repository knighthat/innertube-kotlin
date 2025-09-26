package me.knighthat.innertube.response


interface Badge {

    companion object {
        const val EXPLICIT: String = "MUSIC_EXPLICIT_BADGE"
    }

    val musicInlineBadgeRenderer: MusicInlineBadge?
    val metadataBadgeRenderer: MetadataBadge?

    interface Renderer {

        val style: String?
        val tooltip: String?
        val icon: Icon?
    }

    interface MusicInlineBadge: Renderer {

        val accessibilityData: Accessibility?
    }

    interface MetadataBadge: Renderer {

        val accessibilityData: Accessibility.Data?
    }
}