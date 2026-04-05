package bob.colbaskin.umir_hack_2.profile.domain.models

import java.time.Instant

data class DiplomaLinkHistoryItem(
    val tokenId: Long,
    val diplomaId: Long,
    val diplomaTitle: String,
    val shareUrl: String?,
    val createdAt: Instant,
    val endedAt: Instant?,
    val status: HistoryStatus
)
