package bob.colbaskin.umir_hack_2.profile.domain.models

data class DiplomaTokens(
    val token: String,
    val tokenMeta: QrTokenMeta,
    val diploma: Diploma
)
