package bob.colbaskin.umir_hack_2.profile.presentation.components.share_link

import bob.colbaskin.umir_hack_2.BuildConfig
import javax.inject.Inject

class ShareLinkBuilderImpl @Inject constructor() : ShareLinkBuilder {
    override fun build(tokenString: String): String {
        val base = BuildConfig.BASE_API_URL.trimEnd('/')
        return "$base/diplomas/qr-token?token=$tokenString"
    }
}
