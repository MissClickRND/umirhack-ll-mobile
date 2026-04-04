package bob.colbaskin.umir_hack_2.auth.data

import android.util.Log
import bob.colbaskin.umir_hack_2.auth.data.models.LoginBody
import bob.colbaskin.umir_hack_2.auth.data.models.LogoutDTO
import bob.colbaskin.umir_hack_2.auth.data.models.RegisterBody
import bob.colbaskin.umir_hack_2.auth.data.models.StatusDTO
import bob.colbaskin.umir_hack_2.auth.data.models.UserDTO
import bob.colbaskin.umir_hack_2.auth.domain.AuthApiService
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.common.utils.safeApiCall
import jakarta.inject.Inject

private const val TAG = "Auth"

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,
    private val userPreferences: UserPreferencesRepository,
): AuthRepository {
    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): ApiResult<User> {
        Log.d(TAG, "Attempting register for user: $email")
        return safeApiCall<UserDTO, User>(
            apiCall = {
                authApi.register(
                    body = RegisterBody(
                        email = email,
                        password = password,
                        name = name
                    )
                )
            },
            successHandler = { response ->
                Log.d(TAG, "Register successful. Saving AUTHENTICATED status")
                userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                response.toDomain()
            }
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): ApiResult<User> {
        Log.d(TAG, "Attempting login for user: $email")
        return safeApiCall<UserDTO, User>(
            apiCall = {
                authApi.login(
                    body = LoginBody(
                        email = email,
                        password = password
                    )
                )
            },
            successHandler = { response ->
                Log.d(TAG, "Login successful. Saving AUTHENTICATED status")
                userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                response.toDomain()
            }
        )
    }

    override suspend fun refresh(): ApiResult<User> {
        Log.d(TAG, "Attempting refresh")
        return safeApiCall<UserDTO, User>(
            apiCall = { authApi.refresh() },
            successHandler = { response ->
                Log.d(TAG, "Refresh successful.")
                response.toDomain()
            }
        )
    }

    override suspend fun status(): ApiResult<StatusDTO> {
        Log.d(TAG, "Attempting update auth status")
        return safeApiCall<StatusDTO, StatusDTO>(
            apiCall = { authApi.status() },
            successHandler = { response ->
                if (response.authenticated) {
                    Log.d(TAG, "Update successful. Saving AUTHENTICATED status")
                    userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                } else {
                    Log.d(TAG, "Update successful. Saving NOT_AUTHENTICATEDstatus")
                    userPreferences.saveAuthStatus(AuthConfig.NOT_AUTHENTICATED)
                }
                response
            }
        )
    }

    override suspend fun logout(): ApiResult<LogoutDTO> {
        Log.d(TAG, "Attempting logout")
        return safeApiCall<LogoutDTO, LogoutDTO>(
            apiCall = { authApi.logout() },
            successHandler = { response ->
                Log.d(TAG, "Logout successful. Saving NOT_AUTHENTICATED status")
                userPreferences.saveAuthStatus(AuthConfig.NOT_AUTHENTICATED)
                response
            }
        )
    }
}
