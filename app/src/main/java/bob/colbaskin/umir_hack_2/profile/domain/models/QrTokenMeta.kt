package bob.colbaskin.umir_hack_2.profile.domain.models

import java.time.Instant

data class QrTokenMeta(
    val tokenId: Long,
    val diplomaId: Long,
    val expiresAt: Instant?,
    val revokedAt: Instant?,
    val isOneTime: Boolean,
    val lastUsedAt: Instant?,
    val createdAt: Instant
)
