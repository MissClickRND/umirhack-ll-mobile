package bob.colbaskin.umir_hack_2.profile.domain

import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User

interface ProfileRepository {

    suspend fun getUser(): ApiResult<User>
}