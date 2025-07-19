package me.knighthat.internal.model

import me.knighthat.innertube.decode
import me.knighthat.internal.response.SectionListRendererImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class InnertubeChartsImplTest {

    @Test
    fun testFromSectionListRenderer() {
        val fileName = "ytm/browse/charts_sectionListRenderer.json"
        ClassLoader.getSystemResourceAsStream( fileName )
            .also( ::assertNotNull )
            ?.use { inStream ->
                val renderer = inStream.decode<SectionListRendererImpl>()
                assertNotNull( renderer )

                val charts = InnertubeChartsImpl.from( renderer )
                assertEquals( "United States", charts.selectedCountryName )
                assertEquals( "Select a country", charts.menu.title )
                // Divider not included
                assertEquals( 63, charts.menu.items.size )
                // Menu is excluded
                assertEquals( 3, charts.sections.size )
            }
    }
}