package bob.colbaskin.hack_template.common.user_prefs.domain

import bob.colbaskin.hack_template.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.hack_template.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.hack_template.common.user_prefs.data.models.UserPreferences
import bob.colbaskin.hack_template.profile.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun saveAuthStatus(status: AuthConfig)
    suspend fun saveOnboardingStatus(status: OnboardingConfig)
}
