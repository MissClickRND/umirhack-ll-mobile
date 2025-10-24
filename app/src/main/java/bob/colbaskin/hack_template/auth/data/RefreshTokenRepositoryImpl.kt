package bob.colbaskin.hack_template.auth.data

import android.util.Log
import bob.colbaskin.hack_template.auth.domain.token.RefreshTokenRepository
import bob.colbaskin.hack_template.auth.domain.token.RefreshTokenService
import bob.colbaskin.hack_template.common.ApiResult
import bob.colbaskin.hack_template.common.utils.safeApiCall
import jakarta.inject.Inject
import retrofit2.Response


private const val TAG = "Auth"

class RefreshTokenRepositoryImpl @Inject constructor(
    private val tokenApi: RefreshTokenService
): RefreshTokenRepository {
    override suspend fun refresh(): ApiResult<Unit> {
        Log.d(TAG, "Attempting refresh token")
        return safeApiCall<Response<Unit>, Unit>(
            apiCall = {
                tokenApi.refresh()
            },
            successHandler = { response ->
                Log.d(TAG, "Refresh successful")
                response
            }
        )
    }
}
