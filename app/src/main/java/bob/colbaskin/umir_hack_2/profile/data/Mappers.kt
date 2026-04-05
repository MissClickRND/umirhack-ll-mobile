package bob.colbaskin.umir_hack_2.profile.data

import bob.colbaskin.umir_hack_2.profile.data.models.*
import bob.colbaskin.umir_hack_2.profile.domain.models.*
import java.time.Instant

private fun String.toInstant(): Instant = Instant.parse(this)

private fun String.toInstantOrNull(): Instant? =
    runCatching { Instant.parse(this) }.getOrNull()

fun UniversityDTO.toDomain(): University = University(
    id = id,
    name = name,
    shortName = shortName
)

fun DiplomaDTO.toDomain(): Diploma = Diploma(
    id = id,
    registrationNumber = registrationNumber,
    issuedAt = issuedAt.toInstant(),
    specialty = specialty,
    degreeLevel = when (degreeLevel) {
        "BACHELOR" -> DegreeLevel.BACHELOR
        "MASTER" -> DegreeLevel.MASTER
        "SPECIALIST" -> DegreeLevel.SPECIALIST
        else -> DegreeLevel.UNKNOWN
    },
    status = when (status) {
        "VALID" -> DiplomaStatus.VALID
        "REVOKED" -> DiplomaStatus.REVOKED
        else -> DiplomaStatus.UNKNOWN
    },
    university = university?.toDomain(),
    fullNameAuthor = fullNameAuthor,
    userId = userId,
    universityId = universityId,
    createdAt = Instant.parse(createdAt),
    updatedAt = Instant.parse(updatedAt),
)

fun QrTokenMetaDTO.toDomain(): QrTokenMeta = QrTokenMeta(
    tokenId = id,
    diplomaId = diplomaId,
    expiresAt = expiresAt?.toInstantOrNull(),
    revokedAt = revokedAt?.toInstantOrNull(),
    isOneTime = isOneTime,
    lastUsedAt = lastUsedAt?.toInstantOrNull(),
    createdAt = createdAt.toInstant()
)

fun DiplomaTokensDTO.toDomain(): DiplomaTokens = DiplomaTokens(
    token = token,
    tokenMeta = tokenMeta.toDomain(),
    diploma = diploma.toDomain()
)

fun AttachDiplomaDTO.toDomain(): AttachedDiploma = AttachedDiploma(
    id = id,
    userId = userId,
    universityId = universityId,
    status = when (status) {
        "VALID" -> DiplomaStatus.VALID
        "REVOKED" -> DiplomaStatus.REVOKED
        else -> DiplomaStatus.UNKNOWN
    }
)

fun QrTokenType.toApiType(): String = when (this) {
    QrTokenType.ONETIME -> "ONETIME"
    QrTokenType.DAYS_7 -> "DAYS_7"
    QrTokenType.DAYS_30 -> "DAYS_30"
    QrTokenType.INFINITE -> "INFINITE"
}
