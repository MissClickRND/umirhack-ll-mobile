package bob.colbaskin.umir_hack_2.profile.domain.models

data class ProfileLinkItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val url: String,
    val isActive: Boolean,
    val type: ProfileLinkType
)

enum class ProfileLinkType {
    LINK,
    PDF
} // убрать. ссылка будет генерироваться по токену + ссылке на сайт наш