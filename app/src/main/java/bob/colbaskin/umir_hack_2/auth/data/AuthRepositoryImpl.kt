package bob.colbaskin.umir_hack_2.auth.data

import android.util.Log
import bob.colbaskin.umir_hack_2.auth.data.models.body.LoginBody
import bob.colbaskin.umir_hack_2.auth.data.models.dto.LogoutDTO
import bob.colbaskin.umir_hack_2.auth.data.models.body.RegisterBody
import bob.colbaskin.umir_hack_2.auth.data.models.dto.LoginDTO
import bob.colbaskin.umir_hack_2.auth.data.models.dto.RegisterDTO
import bob.colbaskin.umir_hack_2.auth.data.models.dto.StatusDTO
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
        password: String
    ): ApiResult<User> {
        Log.d(TAG, "Attempting register for user: $email")
        return safeApiCall<RegisterDTO, User>(
            apiCall = {
                authApi.register(
                    body = RegisterBody(
                        accountType = "student",
                        email = email,
                        password = password,
                        name = "",
                        shortName = ""
                    )
                )
            },
            successHandler = { response ->
                Log.d(TAG, "Register successful. Saving AUTHENTICATED status")
                userPreferences.saveRoleStatus(response.user.role.toDomain())
                userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                response.user.toDomain()
            }
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): ApiResult<User> {
        Log.d(TAG, "Attempting login for user: $email")
        return safeApiCall<LoginDTO, User>(
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
                userPreferences.saveRoleStatus(response.user.role.toDomain())
                userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                response.user.toDomain()
            }
        )
    }

    override suspend fun refresh(): ApiResult<Unit> {
        Log.d(TAG, "Attempting refresh")
        return safeApiCall<Unit, Unit>(
            apiCall = { authApi.refresh() },
            successHandler = { response ->
                Log.d(TAG, "Refresh successful.")
            }
        )
    }

    override suspend fun status(): ApiResult<Boolean> {
        Log.d(TAG, "Attempting update auth status")
        return safeApiCall<StatusDTO, Boolean>(
            apiCall = { authApi.status() },
            successHandler = { response ->
                if (response.authenticated) {
                    Log.d(TAG, "Update successful. Saving AUTHENTICATED status")
                    userPreferences.saveAuthStatus(AuthConfig.AUTHENTICATED)
                } else {
                    Log.d(TAG, "Update successful. Saving NOT_AUTHENTICATEDstatus")
                    userPreferences.saveAuthStatus(AuthConfig.NOT_AUTHENTICATED)
                }
                response.authenticated
            }
        )
    }

    override suspend fun logout(): ApiResult<Unit> {
        Log.d(TAG, "Attempting logout")
        return safeApiCall<LogoutDTO, Unit>(
            apiCall = { authApi.logout() },
            successHandler = { response ->
                Log.d(TAG, "Logout successful. Saving NOT_AUTHENTICATED status")
                userPreferences.saveAuthStatus(AuthConfig.NOT_AUTHENTICATED)
            }
        )
    }
}
