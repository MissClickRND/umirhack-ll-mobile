package bob.colbaskin.hack_template.profile.domain

import bob.colbaskin.hack_template.common.ApiResult
import bob.colbaskin.hack_template.profile.domain.models.User

interface ProfileRepository {

    suspend fun getUser(): ApiResult<User>
}