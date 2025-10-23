package bob.colbaskin.hack_template.common.user_prefs.domain.models

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val avatarUrl: String? = null
)
