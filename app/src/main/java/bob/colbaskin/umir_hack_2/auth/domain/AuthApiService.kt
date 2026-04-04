package bob.colbaskin.umir_hack_2.auth.domain

import bob.colbaskin.umir_hack_2.auth.data.models.LoginBody
import bob.colbaskin.umir_hack_2.auth.data.models.LoginDTO
import bob.colbaskin.umir_hack_2.auth.data.models.LogoutDTO
import bob.colbaskin.umir_hack_2.auth.data.models.RefreshDTO
import bob.colbaskin.umir_hack_2.auth.data.models.RegisterBody
import bob.colbaskin.umir_hack_2.auth.data.models.RegisterDTO
import bob.colbaskin.umir_hack_2.auth.data.models.StatusDTO
import bob.colbaskin.umir_hack_2.auth.data.models.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/auth/register")
    suspend fun register(@Body body: RegisterBody): UserDTO

    @POST("/auth/login")
    suspend fun login(@Body body: LoginBody): UserDTO

    @POST("/auth/refresh")
    suspend fun refresh(): UserDTO

    @GET("/auth/status")
    suspend fun status(): StatusDTO

    @POST("/auth/logout")
    suspend fun logout(): LogoutDTO
}
