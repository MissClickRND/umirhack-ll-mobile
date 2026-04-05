package bob.colbaskin.umir_hack_2.profile.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UniversityDTO(
    val id: Long,
    val name: String,
    val shortName: String
)
