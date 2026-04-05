package bob.colbaskin.umir_hack_2.profile.domain.models

import java.time.Instant

data class DiplomaShareLink(
    val tokenId: Long,
    val diplomaId: Long,
    val tokenString: String?,
    val shareUrl: String?,
    val createdAt: Instant,
    val expiresAt: Instant?,
    val revokedAt: Instant?,
    val isOneTime: Boolean,
    val lastUsedAt: Instant?
) {
    val isActive: Boolean
        get() {
            val now = Instant.now()
            if (revokedAt != null) return false
            if (isOneTime) return lastUsedAt == null
            val exp = expiresAt ?: return true
            return exp.isAfter(now)
        }
}
