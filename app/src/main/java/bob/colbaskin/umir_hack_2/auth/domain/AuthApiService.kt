package bob.colbaskin.umir_hack_2.auth.domain

import bob.colbaskin.umir_hack_2.auth.data.models.body.LoginBody
import bob.colbaskin.umir_hack_2.auth.data.models.body.RegisterBody
import bob.colbaskin.umir_hack_2.auth.data.models.dto.LoginDTO
import bob.colbaskin.umir_hack_2.auth.data.models.dto.LogoutDTO
import bob.colbaskin.umir_hack_2.auth.data.models.dto.RegisterDTO
import bob.colbaskin.umir_hack_2.auth.data.models.dto.StatusDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/auth/register")
    suspend fun register(@Body body: RegisterBody): RegisterDTO

    @POST("/auth/login")
    suspend fun login(@Body body: LoginBody): LoginDTO

    @POST("/auth/refresh")
    suspend fun refresh()

    @GET("/auth/status")
    suspend fun status(): StatusDTO

    @POST("/auth/logout")
    suspend fun logout(): LogoutDTO
}
