package me.knighthat.internal.util

import me.knighthat.innertube.Innertube
import me.knighthat.innertube.request.Localization
import me.knighthat.innertube.request.body.Context
import me.knighthat.internal.InnertubeImpl
import org.jetbrains.annotations.Contract


/**
 * Generate **new** [Context] from provided [template].
 *
 * If [useLogin] is `true` then [Innertube.KtorProvider.visitorData]
 * is used along with other properties.
 *
 *
 */
@Contract(
    value = "->new",
    pure = true
)
internal fun InnertubeImpl.getContext(
    template: Context = Context.WEB_REMIX_DEFAULT,
    localization: Localization = Localization.EN_US,
    visitorData: String? = null,
    useLogin: Boolean = false
): Context {
    val visitorData = if( useLogin )
        provider.visitorData
    else visitorData ?: template.client.userAgent

    return Context(
        template.client.copy(
            hl = localization.languageCode,
            gl = localization.regionCode,
            visitorData = visitorData
        ),
        Context.User().copy(
            onBehalfOfUser = if( useLogin ) provider.dataSyncId else null
        )
    )
}