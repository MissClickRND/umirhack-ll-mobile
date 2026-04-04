package bob.colbaskin.umir_hack_2.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogoutDTO(
    @SerialName("refreshToken") val refreshToken: String
) {
}
