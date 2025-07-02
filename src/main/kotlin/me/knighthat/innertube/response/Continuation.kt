package me.knighthat.innertube.response


interface Continuation {

    val nextContinuationData: Data?
    val nextRadioContinuationData: Data?

    interface Data {

        val continuation: String
    }
}
