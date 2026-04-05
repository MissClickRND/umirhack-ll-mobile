package bob.colbaskin.umir_hack_2.profile.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AttachDiplomaDTO(
    val id: Long,
    val userId: Long,
    val universityId: Long,
    val status: String
)
