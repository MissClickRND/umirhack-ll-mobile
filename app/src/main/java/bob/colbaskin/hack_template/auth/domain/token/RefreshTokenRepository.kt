package bob.colbaskin.hack_template.auth.domain.token

import bob.colbaskin.hack_template.common.ApiResult

interface RefreshTokenRepository {

    suspend fun refresh(): ApiResult<Unit>
}
