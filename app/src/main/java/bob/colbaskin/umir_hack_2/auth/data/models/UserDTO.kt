package bob.colbaskin.umir_hack_2.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val email: String,
    val name: String,
    val role: String,
    @SerialName("createdAt") val createdAt: String,
)
