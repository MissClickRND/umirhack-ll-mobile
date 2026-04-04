package bob.colbaskin.umir_hack_2.profile.domain.models

import java.time.Instant

data class DiplomaShareLink(
    val id: String,
    val diplomaId: String,
    val token: String,
    val shortUrl: String,
    val createdAt: Instant,
    val expiresAt: Instant,
    val isRevoked: Boolean = false
) {
    val isActive: Boolean
        get() = !isRevoked && expiresAt.isAfter(Instant.now())
}
