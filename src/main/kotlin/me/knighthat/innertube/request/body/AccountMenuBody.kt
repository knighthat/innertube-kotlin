package me.knighthat.innertube.request.body

import kotlinx.serialization.Serializable

@Serializable
data class AccountMenuBody(
    override val context: Context,
    val deviceTheme: String = "DEVICE_THEME_SELECTED",
    val userInterfaceTheme: String = "USER_INTERFACE_THEME_DARK",
): RequestBody