package bob.colbaskin.umir_hack_2.auth.domain

import bob.colbaskin.umir_hack_2.auth.data.models.LoginDTO
import bob.colbaskin.umir_hack_2.auth.data.models.LogoutDTO
import bob.colbaskin.umir_hack_2.auth.data.models.RefreshDTO
import bob.colbaskin.umir_hack_2.auth.data.models.RegisterDTO
import bob.colbaskin.umir_hack_2.auth.data.models.StatusDTO
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User

interface AuthRepository {

    suspend fun register(
        email: String,
        password: String,
        name: String
    ): ApiResult<User>

    suspend fun login(
        email: String,
        password: String
    ): ApiResult<User>

    suspend fun refresh(): ApiResult<User>

    suspend fun status(): ApiResult<StatusDTO>

    suspend fun logout(): ApiResult<LogoutDTO>
}
