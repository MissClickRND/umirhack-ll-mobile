package bob.colbaskin.umir_hack_2.common.user_prefs.domain.models

data class User(
    val id: Int,
    val email: String,
    val role: Role,
)
