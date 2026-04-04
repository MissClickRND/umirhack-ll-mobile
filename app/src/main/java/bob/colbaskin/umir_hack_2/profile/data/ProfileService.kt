package bob.colbaskin.umir_hack_2.profile.data

import bob.colbaskin.umir_hack_2.auth.data.models.dto.UserDTO
import retrofit2.http.GET

interface ProfileService {

    @GET("/api/v1/user/me")
    suspend fun getUser(): UserDTO
}