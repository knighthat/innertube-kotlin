package me.knighthat.innertube.response


interface Microformat {

    val microformatDataRenderer: Renderer

    interface Renderer {

        val urlCanonical: String
        val title: String?
        val description: String?
        val thumbnail: Thumbnails?
        val siteName: String?
        val appName: String?
        val ogType: String?
        val twitterCardType: String?
        val twitterSiteHandle: String?
        val schemaDotOrgType: String?
        val noindex: Boolean?
        val unlisted: Boolean?
        val paid: Boolean?
        val familySafe: Boolean?
        val tags: List<String>
        val availableCountries: List<String>
        val pageOwnerDetails: PageOwnerDetails?
        val videoDetails: VideoDetails?
        val viewCount: String?
        val publishDate: String?
        val category: String?
        val uploadDate: String?

        interface PageOwnerDetails {

            val name: String
            val externalChannelId: String
            val youtubeProfileUrl: String
        }

        interface VideoDetails {

            val externalVideoId: String
            val durationSeconds: String
            val durationIso8601: String
        }
    }
}