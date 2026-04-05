package bob.colbaskin.umir_hack_2.profile.domain.models

data class ProfileDiploma(
    val id: Long,
    val title: String,
    val qualification: String,
    val year: Int,
    val number: String,
    val universityName: String,
    val issued: Boolean = true
)
