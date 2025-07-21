package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable
import me.knighthat.innertube.Constants
import me.knighthat.innertube.UserAgents
import me.knighthat.innertube.request.Localization

@Serializable
data class Context(
    val client: Client,
    val user: User,
) {

    companion object {
        @JvmField
        val WEB_REMIX_DEFAULT: Context = Context(Client.WEB_REMIX, User())

        @JvmField
        val IOS_DEFAULT: Context = Context(Client.IOS, User())

        @JvmField
        val WEB_DEFAULT: Context = Context(Client.WEB, User())
    }

    /**
     * Client's information
     *
     * @param clientName Name of client to present to YouTube, can be extracted by using web interface
     * @param clientVersion Version co-responding to [clientName], should be extracted at the same time with it to prevent version mismatch
     * @param platform Platform sending the request, co-response to [clientName]
     * @param hl Stands for `host language`, used to tell YT/YTM in which language the response should be
     * @param gl Stands for `geolocation`, can be used to access trending songs, playlists form a specific region
     * @param visitorData Unique string for YT tracking. Required in certain scenarios
     * @param userAgent Browser identifier
     * @param referer Should be **https://music.youtube.com** in most cases
     * @param xClientName [clientVersion] in numeric format. For desktop, it's **67**
     * @param deviceMake Device's brand, can be empty
     * @param deviceModel Device's model, can be empty
     * @param osName Device's operating system, commonly used to spook mobile devices
     * @param osVersion Device's operating system's version, commonly used to spook mobile devices
     * @param originalUrl Usually **https://www.youtube.com/watch?v=$videoId**
     * @param acceptHeader For content-negotiation, not required, but nice to have
     * @param androidSdkVersion Required when Android client is in use
     */
    @Serializable
    data class Client(
        val clientName: String,
        val clientVersion: String,
        val platform: String,
        val hl: String,
        val gl: String,
        val visitorData: String,
        val userAgent: String?,
        val referer: String?,
        val xClientName: Int,
        val deviceMake: String,
        val deviceModel: String,
        val osName: String,
        val osVersion: String,
        val originalUrl: String?,
        val acceptHeader: String?,
        val androidSdkVersion: Int?
    ) {

        companion object {

            @JvmField
            val WEB_REMIX: Client = Client(
                "WEB_REMIX",
                "1.20250416.01.00",
                "DESKTOP",
                Localization.EN_US,
                Constants.VISITOR_DATA,
                UserAgents.CHROME_WINDOWS,
                Constants.YOUTUBE_MUSIC_URL,
                67,
                "",
                "",
                "",
                "",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                Constants.ACCEPT_HEADERS,
                null
            )

            @JvmField
            val IOS: Client = Client(
                "IOS",
                "20.14.2",
                "MOBILE",
                Localization.EN_US,
                Constants.VISITOR_DATA,
                UserAgents.IOS,
                Constants.YOUTUBE_MUSIC_URL,
                5,
                "Apple",
                "iPhone15,4",
                "iOS",
                "17.4.1.21E237",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                Constants.ACCEPT_HEADERS,
                null
            )

            @JvmField
            val WEB: Client = Client(
                "WEB",
                "2.20250523.01.00",
                "DESKTOP",
                Localization.EN_US,
                Constants.VISITOR_DATA,
                UserAgents.CHROME_WINDOWS,
                Constants.YOUTUBE_URL,
                1,
                "",
                "",
                "Windows",
                "",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                Constants.ACCEPT_HEADERS,
                null
            )
        }

        constructor(
            clientName: String,
            clientVersion: String,
            platform: String,
            localization: Localization,
            visitorData: String,
            userAgent: String?,
            referer: String?,
            xClientName: Int,
            deviceMake: String,
            deviceModel: String,
            osName: String,
            osVersion: String,
            originalUrl: String?,
            acceptHeader: String?,
            androidSdkVersion: Int?
        ): this(
            clientName,
            clientVersion,
            platform,
            localization.languageCode,
            localization.regionCode,
            visitorData,
            userAgent,
            referer,
            xClientName,
            deviceMake,
            deviceModel,
            osName,
            osVersion,
            originalUrl,
            acceptHeader,
            androidSdkVersion
        )
    }

    @Serializable
    data class User(
        val lockedSafetyMode: Boolean = false,
        val onBehalfOfUser: String? = null,
    )
}
