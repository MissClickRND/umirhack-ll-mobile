package bob.colbaskin.umir_hack_2.scanner.data.models

import bob.colbaskin.umir_hack_2.scanner.domain.models.ScannerResult

object ScannerResultMapper {

    fun toDomain(dto: DocumentCheckResponse): ScannerResult {
        return ScannerResult(
            documentId = dto.id,
            status = dto.status,
            expirationDate = dto.expirationDate,
            createdAt = dto.createdAt,
            checkedAt = dto.checkedAt
        )
    }
}
