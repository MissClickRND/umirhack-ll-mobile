package bob.colbaskin.hack_template.profile.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val email: String
)
