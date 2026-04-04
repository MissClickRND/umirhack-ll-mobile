package bob.colbaskin.umir_hack_2.common.user_prefs.domain

import bob.colbaskin.umir_hack_2.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.umir_hack_2.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.umir_hack_2.common.user_prefs.data.models.UserPreferences
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.Role
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>

    suspend fun saveAuthStatus(status: AuthConfig)

    suspend fun saveRoleStatus(status: Role)

    suspend fun saveOnboardingStatus(status: OnboardingConfig)
}
