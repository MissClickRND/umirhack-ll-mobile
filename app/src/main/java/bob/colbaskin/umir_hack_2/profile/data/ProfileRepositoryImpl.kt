package bob.colbaskin.umir_hack_2.profile.data

import android.util.Log
import bob.colbaskin.umir_hack_2.auth.data.models.UserDTO
import bob.colbaskin.umir_hack_2.auth.data.toDomain
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.common.utils.safeApiCall
import bob.colbaskin.umir_hack_2.profile.domain.ProfileRepository
import bob.colbaskin.umir_hack_2.profile.domain.ProfileService
import java.time.Instant
import javax.inject.Inject

private const val  TAG = "Profile"

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileService
): ProfileRepository {
    override suspend fun getUser(): ApiResult<User> {
        Log.d(TAG, "Attempting getting User")
//        return safeApiCall<UserDTO, User>(
//            apiCall = { profileApi.getUser() },
//            successHandler = { response ->
//                val user = response.toDomain()
//                Log.d(TAG, "User got successful: $user")
//                user
//            }
//        )
        return ApiResult.Success(
            User(
                id = 1,
                email = "babakapa729@gmail.com",
                name = "bybus",
                role = "student",
                createdAt = Instant.now()
            )
        )
    }
}
