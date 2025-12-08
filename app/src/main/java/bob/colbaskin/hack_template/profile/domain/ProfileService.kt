package bob.colbaskin.hack_template.profile.domain

import bob.colbaskin.hack_template.profile.data.models.UserDTO
import retrofit2.http.GET

interface ProfileService {

    @GET("/api/v1/user/me")
    suspend fun getUser(): UserDTO
}