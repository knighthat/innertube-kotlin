package me.knighthat.innertube.response

interface ActiveAccountHeaderRenderer {

    val accountName: Runs
    val email: Runs?
    val channelHandle: Runs?
    val accountPhoto: Thumbnails
}