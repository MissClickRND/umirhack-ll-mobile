package bob.colbaskin.umir_hack_2.scanner.data.remote

import android.util.Log
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.utils.safeApiCall
import bob.colbaskin.umir_hack_2.scanner.data.models.DocumentCheckResponse
import bob.colbaskin.umir_hack_2.scanner.data.models.ScannerResultMapper
import bob.colbaskin.umir_hack_2.scanner.domain.ScannerRepository
import bob.colbaskin.umir_hack_2.scanner.domain.models.ScannerResult
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ScannerRepositoryImpl"

@Singleton
class ScannerRepositoryImpl @Inject constructor(
    private val documentApi: DocumentApi,
) : ScannerRepository {

    override suspend fun checkDocument(hash: String): ApiResult<ScannerResult> {
        Log.d(TAG, "Checking document for hash: ${hash.take(20)}...")

        return safeApiCall<Response<DocumentCheckResponse>, ScannerResult>(
            apiCall = {
                documentApi.checkDocument(hash)
            },
            successHandler = { response ->
                val dto = response.body()!!
                val domainResult = ScannerResultMapper.toDomain(dto)
                saveScanResult(domainResult)
                domainResult
            }
        )
    }

    override suspend fun saveScanResult(result: ScannerResult) {
        TODO()
    }

    override suspend fun getAllScanResults(): List<ScannerResult> {
        TODO()
    }
}
