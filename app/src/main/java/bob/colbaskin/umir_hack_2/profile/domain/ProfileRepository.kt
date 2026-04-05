package bob.colbaskin.umir_hack_2.profile.domain

import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.profile.domain.models.AttachedDiploma
import bob.colbaskin.umir_hack_2.profile.domain.models.Diploma
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaTokens
import bob.colbaskin.umir_hack_2.profile.domain.models.QrTokenType

interface ProfileRepository {

    suspend fun getUser(): ApiResult<User>

    suspend fun getUserDiplomas(userId: Long): ApiResult<List<Diploma>>

    suspend fun getUserTokens(userId: Long): ApiResult<List<DiplomaTokens>>

    suspend fun createQrToken(
        diplomaId: Long,
        type: QrTokenType
    ): ApiResult<String>

    suspend fun getDiplomaByQrToken(token: String): ApiResult<Diploma>

    suspend fun revokeQrToken(tokenId: Long): ApiResult<Unit>

    suspend fun attachDiplomaToMe(diplomaId: Long): ApiResult<AttachedDiploma>

    suspend fun searchDiplomaByNumber(
        number: String,
        fullName : String
    ): ApiResult<Diploma>
}
