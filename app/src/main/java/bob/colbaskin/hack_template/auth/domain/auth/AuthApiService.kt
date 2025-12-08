package bob.colbaskin.hack_template.auth.domain.auth

import bob.colbaskin.hack_template.auth.data.models.LoginBody
import bob.colbaskin.hack_template.auth.data.models.LoginDTO
import bob.colbaskin.hack_template.auth.data.models.RegisterBody
import bob.colbaskin.hack_template.auth.data.models.RegisterDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body body: LoginBody
    ): LoginDTO

    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body body: RegisterBody
    ): RegisterDTO
}
