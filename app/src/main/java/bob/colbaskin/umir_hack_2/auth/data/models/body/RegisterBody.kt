package bob.colbaskin.umir_hack_2.auth.data.models.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterBody(
    val accountType: String,
    val email: String,
    val password: String,
    val name: String,
    @SerialName("short_name") val shortName: String
)
