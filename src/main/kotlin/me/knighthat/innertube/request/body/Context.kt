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

        @JvmField
        val TVHTML5_EMBEDDED_PLAYER_DEFAULT = Context(Client.TVHTML5_EMBEDDED_PLAYER, User())

        @JvmField
        val ANDROID_DEFAULT = Context(Client.ANDROID, User())

        @JvmField
        val ANDROID_VR_DEFAULT = Context(Client.ANDROID_VR, User())
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
        val hl: String,
        val gl: String,
        val visitorData: String?,
        val userAgent: String,
        val xClientName: Int,
        val referer: String? = null,
        val clientId: String = xClientName.toString(),
        val deviceMake: String? = null,
        val deviceModel: String? = null,
        val osName: String? = null,
        val osVersion: String? = null,
        val originalUrl: String? = null,
        val acceptHeader: String = Constants.ACCEPT_HEADERS,
        val androidSdkVersion: Int? = null
    ) {

        companion object {

            @JvmField
            val WEB_REMIX: Client = Client(
                clientName = "WEB_REMIX",
                clientVersion = "1.20250416.01.00",
                hl = Localization.EN_US.languageCode,
                gl = Localization.EN_US.regionCode,
                visitorData = Constants.CHROME_WINDOWS_VISITOR_DATA,
                userAgent = UserAgents.CHROME_WINDOWS,
                xClientName = 67,
                referer = Constants.YOUTUBE_MUSIC_URL,
                clientId = Constants.YOUTUBE_MUSIC_URL
            )

            @JvmField
            val IOS: Client = Client(
                clientName = "IOS",
                clientVersion = "20.14.2",
                hl = Localization.EN_US.languageCode,
                gl = Localization.EN_US.regionCode,
                visitorData = Constants.IOS_VISITOR_DATA,
                userAgent = UserAgents.IOS,
                xClientName = 5,
                deviceMake = "Apple",
                deviceModel = "iPhone15,4",
                osName = "iOS",
                osVersion = "17.4.1.21E237"
            )

            @JvmField
            val WEB: Client = Client(
                clientName = "WEB",
                clientVersion = "2.20250523.01.00",
                hl = Localization.EN_US.languageCode,
                gl = Localization.EN_US.regionCode,
                visitorData = Constants.CHROME_WINDOWS_VISITOR_DATA,
                userAgent = UserAgents.CHROME_WINDOWS,
                xClientName = 1,
                referer = Constants.YOUTUBE_URL,
                originalUrl = Constants.YOUTUBE_URL
            )

            @JvmField
            val TVHTML5_EMBEDDED_PLAYER = Client(
                clientName = "TVHTML5_SIMPLY_EMBEDDED_PLAYER",
                clientVersion = "2.0",
                hl = Localization.EN_US.languageCode,
                gl = Localization.EN_US.regionCode,
                visitorData = Constants.TVHTML5_VISITOR_DATA,
                userAgent = UserAgents.TVHTML5_SIMPLY_EMBEDDED_PLAYER,
                xClientName = 85
            )

            @JvmField
            val ANDROID = Client(
                clientName = "ANDROID",
                clientVersion = "20.10.38",
                hl = Localization.EN_US.languageCode,
                gl = Localization.EN_US.regionCode,
                visitorData = Constants.ANDROID_VISITOR_DATA,
                userAgent = UserAgents.ANDROID,
                xClientName = 3,
                androidSdkVersion = 35
            )

            @JvmField
            val ANDROID_VR = Client(
                clientName = "ANDROID_VR",
                clientVersion = "1.61.48",
                hl = Localization.EN_US.languageCode,
                gl = Localization.EN_US.regionCode,
                visitorData = Constants.ANDROID_VR_VISITOR_DATA,
                userAgent = UserAgents.ANDROID_VR,
                xClientName = 3
            )
        }
    }

    @Serializable
    data class User(
        val lockedSafetyMode: Boolean = false,
        val onBehalfOfUser: String? = null,
    )
}
