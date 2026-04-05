package bob.colbaskin.umir_hack_2.profile.data

import bob.colbaskin.umir_hack_2.profile.data.models.AttachDiplomaDTO
import bob.colbaskin.umir_hack_2.profile.data.models.CreateQrTokenRequestDTO
import bob.colbaskin.umir_hack_2.profile.data.models.CreateQrTokenResponseDTO
import bob.colbaskin.umir_hack_2.profile.data.models.DiplomaDTO
import bob.colbaskin.umir_hack_2.profile.data.models.DiplomaTokensDTO
import bob.colbaskin.umir_hack_2.profile.data.models.RevokeQrTokenResponseDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DiplomasService {

    @GET("/diplomas/user/{userId}")
    suspend fun getUserDiplomas(@Path("userId") userId: Long): List<DiplomaDTO>

    @GET("/diplomas/{userId}/list")
    suspend fun getUserTokens(@Path("userId") userId: Long): List<DiplomaTokensDTO>

    @POST("/diplomas/{id}/qr-token")
    suspend fun createQrToken(
        @Path("id") diplomaId: Long,
        @Body body: CreateQrTokenRequestDTO
    ): CreateQrTokenResponseDTO

    @DELETE("/diplomas/qr-token/{tokenId}")
    suspend fun revokeQrToken(@Path("tokenId") tokenId: Long): RevokeQrTokenResponseDTO

    @GET("/diplomas/qr-token")
    suspend fun getDiplomaByQrToken(
        @Query("token") token: String
    ): DiplomaDTO

    @GET("/diplomas/search")
    suspend fun searchDiplomaByNumber(
        @Query("number") number: String,
        @Query("fullName") fullName: String
    ): DiplomaDTO

    @PATCH("/users/me/diplomas/{id}/attach")
    suspend fun attachDiploma(@Path("id") diplomaId: Long): AttachDiplomaDTO
}
