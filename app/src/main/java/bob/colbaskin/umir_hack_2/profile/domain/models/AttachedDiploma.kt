package bob.colbaskin.umir_hack_2.profile.domain.models

data class AttachedDiploma(
    val id: Long,
    val userId: Long,
    val universityId: Long,
    val status: DiplomaStatus
)
