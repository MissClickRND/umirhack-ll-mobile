package bob.colbaskin.umir_hack_2.profile.presentation.components.share_link

interface ShareLinkBuilder {
    fun build(tokenString: String): String
}