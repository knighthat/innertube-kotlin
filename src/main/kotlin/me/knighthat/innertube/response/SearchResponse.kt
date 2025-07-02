package me.knighthat.innertube.response


interface SearchResponse : InnertubeResponse {

    val contents: Contents

    interface Contents {

        val tabbedSearchResultsRenderer: Tabs
    }
}