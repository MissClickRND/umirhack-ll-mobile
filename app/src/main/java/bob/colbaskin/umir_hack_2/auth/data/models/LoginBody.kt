package bob.colbaskin.umir_hack_2.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    val email: String,
    val password: String
)
