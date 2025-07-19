package me.knighthat.innertube.model

import me.knighthat.innertube.Localized

interface InnertubeCharts {

    val selectedCountryName: String
    val menu: Menu
    val sections: List<Section>

    interface Menu {

        val title: String
        val items: List<Item>

        interface Item {

            val countryDisplayName: String
            val countryCode: String
        }
    }

    interface Section {

        @get:Localized
        val title: String

        val contents: List<InnertubeItem>
    }
}