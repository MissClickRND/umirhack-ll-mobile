package bob.colbaskin.umir_hack_2.profile.data

import android.util.Log
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.profile.domain.ProfileRepository
import bob.colbaskin.umir_hack_2.profile.domain.models.Diploma
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaTokens
import bob.colbaskin.umir_hack_2.profile.domain.models.QrTokenType
import javax.inject.Inject
import bob.colbaskin.umir_hack_2.auth.data.models.dto.StatusDTO
import bob.colbaskin.umir_hack_2.auth.data.toDomain
import bob.colbaskin.umir_hack_2.auth.domain.AuthApiService
import bob.colbaskin.umir_hack_2.common.utils.safeApiCall
import bob.colbaskin.umir_hack_2.profile.data.models.AttachDiplomaDTO
import bob.colbaskin.umir_hack_2.profile.data.models.CreateQrTokenRequestDTO
import bob.colbaskin.umir_hack_2.profile.data.models.CreateQrTokenResponseDTO
import bob.colbaskin.umir_hack_2.profile.data.models.DiplomaDTO
import bob.colbaskin.umir_hack_2.profile.data.models.RevokeQrTokenResponseDTO
import bob.colbaskin.umir_hack_2.profile.domain.models.*

private const val TAG = "Profile"

class ProfileRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,
    private val diplomasApi: DiplomasService,
) : ProfileRepository {

    override suspend fun getUser(): ApiResult<User> {
        Log.d(TAG, "Attempting getting User via status()")

        val statusResult: ApiResult<StatusDTO> = safeApiCall<StatusDTO, StatusDTO>(
            apiCall = { authApi.status() },
            successHandler = { response ->
                response
            }
        )

        return when (statusResult) {
            is ApiResult.Success -> {
                val status = statusResult.data
                if (!status.authenticated) {
                    Log.d(TAG, "status() -> authenticated=false")
                    ApiResult.Error(title = "NOT_AUTHENTICATED", text = "User is not authenticated")
                } else {
                    val user = status.user.toDomain()
                    Log.d(TAG, "status() -> User got successful: $user")
                    ApiResult.Success(user)
                }
            }

            is ApiResult.Error -> {
                Log.d(TAG, "status() error: ${statusResult.text}")
                statusResult
            }
        }
    }

    override suspend fun getUserDiplomas(userId: Long): ApiResult<List<Diploma>> {
        Log.d(TAG, "Attempting getting diplomas for userId=$userId")

        return safeApiCall<List<DiplomaDTO>, List<Diploma>>(
            apiCall = { diplomasApi.getUserDiplomas(userId) },
            successHandler = { response ->
                response.map { it.toDomain() }
            }
        )
    }

    override suspend fun getUserTokens(userId: Long): ApiResult<List<DiplomaTokens>> {
        Log.d(TAG, "Attempting getting tokens list for userId=$userId")

        return safeApiCall<List<bob.colbaskin.umir_hack_2.profile.data.models.DiplomaTokensDTO>, List<DiplomaTokens>>(
            apiCall = { diplomasApi.getUserTokens(userId) },
            successHandler = { response ->
                response.map { it.toDomain() }
            }
        )
    }

    override suspend fun createQrToken(
        diplomaId: Long,
        type: QrTokenType
    ): ApiResult<String> {
        Log.d(TAG, "Attempting create QR token for diplomaId=$diplomaId type=$type")

        return safeApiCall<CreateQrTokenResponseDTO, String>(
            apiCall = {
                diplomasApi.createQrToken(
                    diplomaId = diplomaId,
                    body = CreateQrTokenRequestDTO(
                        type = type.toApiType()
                    )
                )
            },
            successHandler = { response ->
                response.token
            }
        )
    }

    override suspend fun revokeQrToken(tokenId: Long): ApiResult<Unit> {
        Log.d(TAG, "Attempting revoke QR token tokenId=$tokenId")

        return safeApiCall<RevokeQrTokenResponseDTO, Unit>(
            apiCall = { diplomasApi.revokeQrToken(tokenId) },
            successHandler = {

            }
        )
    }

    override suspend fun getDiplomaByQrToken(token: String): ApiResult<Diploma> {
        Log.d(TAG, "Attempting get diploma by QR token")
        return safeApiCall<DiplomaDTO, Diploma>(
            apiCall = { diplomasApi.getDiplomaByQrToken(token) },
            successHandler = { dto -> dto.toDomain() }
        )
    }

    override suspend fun attachDiplomaToMe(diplomaId: Long): ApiResult<AttachedDiploma> {
        Log.d(TAG, "Attempting attach diploma diplomaId=$diplomaId")

        return safeApiCall<AttachDiplomaDTO, AttachedDiploma>(
            apiCall = { diplomasApi.attachDiploma(diplomaId) },
            successHandler = { response ->
                response.toDomain()
            }
        )
    }

    override suspend fun searchDiplomaByNumber(
        number: String,
        fullName: String
    ): ApiResult<Diploma> {
        Log.d(TAG, "Attempting search diploma by number=$number")

        return safeApiCall<DiplomaDTO, Diploma>(
            apiCall = { diplomasApi.searchDiplomaByNumber(number, fullName) },
            successHandler = { dto -> dto.toDomain() }
        )
    }
}
