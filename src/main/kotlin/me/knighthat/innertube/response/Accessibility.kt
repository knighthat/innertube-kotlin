package me.knighthat.innertube.response


interface Accessibility {

    val accessibilityData: Data

    interface Data {

        /**
         * Contains detail text, mostly used in text-to-speech
         */
        val label: String
    }
}