package bob.colbaskin.hack_template.profile.data

import android.util.Log
import bob.colbaskin.hack_template.common.ApiResult
import bob.colbaskin.hack_template.common.utils.safeApiCall
import bob.colbaskin.hack_template.profile.data.models.UserDTO
import bob.colbaskin.hack_template.profile.data.models.toDomain
import bob.colbaskin.hack_template.profile.domain.models.User
import bob.colbaskin.hack_template.profile.domain.ProfileRepository
import bob.colbaskin.hack_template.profile.domain.ProfileService
import javax.inject.Inject

private const val  TAG = "Profile"

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileService
): ProfileRepository {
    override suspend fun getUser(): ApiResult<User> {
        Log.d(TAG, "Attempting getting User")
        return safeApiCall<UserDTO, User>(
            apiCall = { profileApi.getUser() },
            successHandler = { response ->
                val user = response.toDomain()
                Log.d(TAG, "User got successful: $user")
                user
            }
        )
    }
}
