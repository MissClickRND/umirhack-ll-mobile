package bob.colbaskin.umir_hack_2.auth.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val email: String,
    val role: String
)
