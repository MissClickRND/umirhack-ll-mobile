package bob.colbaskin.umir_hack_2.auth.domain

import bob.colbaskin.umir_hack_2.auth.data.models.dto.LogoutDTO
import bob.colbaskin.umir_hack_2.auth.data.models.dto.StatusDTO
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User

interface AuthRepository {

    suspend fun register(
        email: String,
        password: String
    ): ApiResult<User>

    suspend fun login(
        email: String,
        password: String
    ): ApiResult<User>

    suspend fun refresh(): ApiResult<Unit>

    suspend fun status(): ApiResult<Boolean>

    suspend fun logout(): ApiResult<Unit>
}
