package bob.colbaskin.hack_template.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    val email: String,
    val password: String
)
