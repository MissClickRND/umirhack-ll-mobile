package bob.colbaskin.hack_template.auth.domain.auth

import bob.colbaskin.hack_template.common.ApiResult

interface AuthRepository {

    suspend fun login(email: String, password: String): ApiResult<Unit>

    suspend fun register(email: String, password: String): ApiResult<Unit>
}
