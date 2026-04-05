package bob.colbaskin.umir_hack_2.profile.domain.models

import java.time.Instant

data class Diploma(
    val id: Long,
    val fullNameAuthor: String,
    val registrationNumber: String,
    val userId: Long?,
    val universityId: Long,
    val issuedAt: Instant,
    val specialty: String,
    val degreeLevel: DegreeLevel,
    val status: DiplomaStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val university: University?
)
