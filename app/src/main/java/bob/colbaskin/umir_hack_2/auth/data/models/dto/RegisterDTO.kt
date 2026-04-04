package bob.colbaskin.umir_hack_2.auth.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(val user: UserDTO)
