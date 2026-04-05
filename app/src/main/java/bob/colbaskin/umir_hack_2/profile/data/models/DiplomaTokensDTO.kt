package bob.colbaskin.umir_hack_2.profile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiplomaTokensDTO(
    val token: String,
    @SerialName("tokenMeta") val tokenMeta: QrTokenMetaDTO,
    val diploma: DiplomaDTO
)
