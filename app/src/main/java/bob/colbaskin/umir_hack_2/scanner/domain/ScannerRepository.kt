package bob.colbaskin.umir_hack_2.scanner.domain

import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.scanner.domain.models.ScannerResult

interface ScannerRepository {
    suspend fun checkDocument(hash: String): ApiResult<ScannerResult>
    suspend fun saveScanResult(result: ScannerResult)
    suspend fun getAllScanResults(): List<ScannerResult>
}
