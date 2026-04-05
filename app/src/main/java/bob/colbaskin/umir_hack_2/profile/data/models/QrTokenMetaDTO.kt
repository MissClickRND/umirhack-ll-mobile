package bob.colbaskin.umir_hack_2.profile.data.models

import kotlinx.serialization.Serializable

@Serializable
data class QrTokenMetaDTO (
    val id: Long,
    val diplomaId: Long,
    val expiresAt: String?,
    val revokedAt: String?,
    val isOneTime: Boolean,
    val lastUsedAt: String?,
    val createdAt: String
)
