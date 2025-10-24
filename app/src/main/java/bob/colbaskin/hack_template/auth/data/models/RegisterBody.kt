package bob.colbaskin.hack_template.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterBody(
    val email: String,
    val password: String
)
