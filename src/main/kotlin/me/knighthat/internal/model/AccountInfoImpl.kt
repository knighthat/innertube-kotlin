package me.knighthat.internal.model

import me.knighthat.innertube.model.AccountInfo
import me.knighthat.innertube.response.ActiveAccountHeaderRenderer
import me.knighthat.innertube.response.Thumbnails

data class AccountInfoImpl(
    override val name: String,
    override val email: String?,
    override val channelHandle: String?,
    override val thumbnailUrl: List<Thumbnails.Thumbnail>
): AccountInfo {

    companion object {

        internal fun from( renderer: ActiveAccountHeaderRenderer ): AccountInfoImpl =
            AccountInfoImpl(
                renderer.accountName.firstText,
                renderer.email?.firstText,
                renderer.channelHandle?.firstText,
                renderer.accountPhoto.thumbnails
            )
    }
}