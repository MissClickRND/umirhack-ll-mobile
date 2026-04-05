package bob.colbaskin.umir_hack_2.profile.data.models

import kotlinx.serialization.Serializable

@Serializable
data class DiplomaDTO(
    val id: Long,
    val fullNameAuthor: String,
    val registrationNumber: String,
    val userId: Long?,
    val universityId: Long,
    val issuedAt: String,
    val specialty: String,
    val degreeLevel: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val university: UniversityDTO? = null
)
