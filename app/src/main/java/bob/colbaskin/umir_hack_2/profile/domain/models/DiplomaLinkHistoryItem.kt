package bob.colbaskin.umir_hack_2.profile.domain.models

import java.time.Instant

data class DiplomaLinkHistoryItem(
    val id: String,
    val diplomaId: String,
    val diplomaTitle: String,
    val shortUrl: String,
    val createdAt: Instant,
    val expiresAt: Instant,
    val wasRevoked: Boolean
)
