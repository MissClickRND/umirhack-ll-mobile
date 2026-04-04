package bob.colbaskin.umir_hack_2.common.user_prefs.domain.models

import java.time.Instant

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val role: String,
    val createdAt: Instant,
)
