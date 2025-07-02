package me.knighthat.innertube.response


interface Runs {

    val runs: List<Run>
    val accessibility: Accessibility?

    interface Run {

        val bold: Boolean?
        val text: String
        val navigationEndpoint: Endpoint?
    }
}