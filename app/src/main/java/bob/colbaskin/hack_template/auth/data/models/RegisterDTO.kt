package bob.colbaskin.hack_template.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(
    val id: Int,
    val email: String
)
