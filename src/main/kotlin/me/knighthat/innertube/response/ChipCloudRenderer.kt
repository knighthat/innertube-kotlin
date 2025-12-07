package me.knighthat.innertube.response

interface ChipCloudRenderer {

    val chips: List<Chip>

    interface Chip {

        val chipCloudChipRenderer: Renderer

        interface Renderer {

            val text: Runs?
            val navigationEndpoint: Endpoint
            val onDeselectedCommand: Endpoint?
        }
    }
}