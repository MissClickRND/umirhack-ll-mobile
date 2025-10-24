package bob.colbaskin.hack_template.auth.data

import android.util.Log
import bob.colbaskin.hack_template.auth.data.models.LoginBody
import bob.colbaskin.hack_template.auth.data.models.RegisterBody
import bob.colbaskin.hack_template.auth.data.models.RegisterDTO
import bob.colbaskin.hack_template.auth.domain.auth.AuthApiService
import bob.colbaskin.hack_template.auth.domain.auth.AuthRepository
import bob.colbaskin.hack_template.common.ApiResult
import bob.colbaskin.hack_template.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.hack_template.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.hack_template.common.utils.safeApiCall
import jakarta.inject.Inject
import retrofit2.Response

private const val TAG = "Auth"

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,
    private val userPreferences: UserPreferencesRepository,
): AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): ApiResult<Unit> {
        Log.d(TAG, "Attempting login for user: $email")
        return safeApiCall<Response<Unit>, Unit>(
            apiCall = {
                authApi.login(
                    body = LoginBody(
                        email = email,
                        password = password
                    )
                )
            },
            successHandler = { response ->
                Log.d(TAG, "Login successful. Saving Authenticated status")
                userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                response
            }
        )
    }

    override suspend fun register(
        email: String,
        password: String
    ): ApiResult<Unit> {
        Log.d(TAG, "Attempting register for user: $email")
        return safeApiCall<RegisterDTO, Unit>(
            apiCall = {
                authApi.register(
                    body = RegisterBody(
                        email = email,
                        password = password
                    )
                )
            },
            successHandler = { response ->
                Log.d(TAG, "Register successful. Saving Authenticated status")
                userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                response
            }
        )
    }
}
