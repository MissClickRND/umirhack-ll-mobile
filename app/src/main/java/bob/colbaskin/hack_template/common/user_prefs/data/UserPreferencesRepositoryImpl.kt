package bob.colbaskin.hack_template.common.user_prefs.data

import bob.colbaskin.hack_template.common.user_prefs.data.datastore.UserDataStore
import bob.colbaskin.hack_template.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.hack_template.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.hack_template.common.user_prefs.data.models.UserPreferences
import bob.colbaskin.hack_template.profile.domain.ProfileService
import bob.colbaskin.hack_template.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.hack_template.profile.domain.models.User
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserDataStore
): UserPreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> = dataStore.getUserPreferences()

    override suspend fun saveAuthStatus(status: AuthConfig) = dataStore.saveAuthStatus(status)

    override suspend fun saveOnboardingStatus(status: OnboardingConfig)
            = dataStore.saveOnboardingStatus(status)
}
