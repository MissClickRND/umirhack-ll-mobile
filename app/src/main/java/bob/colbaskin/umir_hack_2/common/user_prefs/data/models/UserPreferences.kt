package bob.colbaskin.umir_hack_2.common.user_prefs.data.models

import bob.colbaskin.umir_hack_2.datastore.OnboardingStatus
import bob.colbaskin.umir_hack_2.datastore.AuthStatus
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.Role
import bob.colbaskin.umir_hack_2.datastore.RoleStatus
import bob.colbaskin.umir_hack_2.datastore.UserPreferencesProto

data class UserPreferences(
    val role: Role,
    val onboardingStatus: OnboardingConfig,
    val authStatus: AuthConfig
)

fun UserPreferencesProto.toData(): UserPreferences {
    return UserPreferences(
        role = when (this.roleStatus) {
            RoleStatus.STUDENT -> Role.STUDENT
            RoleStatus.GUEST -> Role.GUEST
            RoleStatus.UNRECOGNIZED, null -> Role.GUEST
        },
        onboardingStatus = when (this.onboardingStatus) {
            OnboardingStatus.NOT_STARTED -> OnboardingConfig.NOT_STARTED
            OnboardingStatus.IN_PROGRESS -> OnboardingConfig.IN_PROGRESS
            OnboardingStatus.COMPLETED -> OnboardingConfig.COMPLETED
            OnboardingStatus.UNRECOGNIZED, null -> OnboardingConfig.NOT_STARTED
        },
        authStatus = when (this.authStatus) {
            AuthStatus.AUTHENTICATED -> AuthConfig.AUTHENTICATED
            AuthStatus.NOT_AUTHENTICATED -> AuthConfig.NOT_AUTHENTICATED
            AuthStatus.UNRECOGNIZED, null -> AuthConfig.NOT_AUTHENTICATED
        }
    )
}
