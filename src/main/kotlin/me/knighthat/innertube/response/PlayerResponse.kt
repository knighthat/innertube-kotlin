package me.knighthat.innertube.response


interface PlayerResponse : InnertubeResponse {

    val playabilityStatus: PlayabilityStatus
    val streamingData: StreamingData?
    val videoDetails: VideoDetails?
    val playerConfig: PlayerConfig?
    val microformat: Microformat?

    interface PlayabilityStatus {

        val status: String
        val reason: String?
        val playableInEmbed: Boolean
        val audioOnlyPlayability: AudioOnlyPlayability?
        val contextParams: String?

        interface AudioOnlyPlayability {

            val audioOnlyPlayabilityRenderer: AudioOnlyPlayabilityRenderer

            interface AudioOnlyPlayabilityRenderer {

                val audioOnlyAvailability: String
            }
        }
    }

    interface StreamingData {

        val expiresInSeconds: String
        val formats: List<Format>
        val adaptiveFormats: List<Format>
        val serverAbrStreamingUrl: String?

        interface Format {

            val itag: Short
            val url: String?
            val mimeType: String
            val bitrate: Int
            val width: Short
            val height: Short
            val lastModified: String
            val contentLength: String?
            val quality: String
            val fps: Byte
            val qualityLabel: String
            val projectionType: String
            val averageBitrate: Int?
            val highReplication: Boolean?
            val audioQuality: String
            val approxDurationMs: String
            val audioSampleRate: String
            val audioChannels: Byte
            val loudnessDb: Float?
            val signatureCipher: String?
        }
    }

    interface VideoDetails {

        val videoId: String
        val title: String
        val lengthSeconds: String
        val channelId: String
        val isOwnerViewing: Boolean
        val isCrawlable: Boolean
        val thumbnail: Thumbnails
        val allowRatings: Boolean
        val viewCount: String
        val author: String
        val isPrivate: Boolean
        val isUnpluggedCorpus: Boolean
        val musicVideoType: String
        val isLiveContent: Boolean
    }

    interface PlayerConfig {

        val audioConfig: AudioConfig

        interface AudioConfig {

            val loudnessDb: Float
            val perceptualLoudnessDb: Float
            val enablePerFormatLoudness: Boolean?
        }
    }
}