package bob.colbaskin.umir_hack_2.profile.presentation

import bob.colbaskin.umir_hack_2.common.UiState
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaLinkHistoryItem
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareDuration
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareLink
import bob.colbaskin.umir_hack_2.profile.domain.models.ProfileDiploma

data class ProfileState(
    val userState: UiState<User> = UiState.Loading,
    val selectedTab: ProfileTab = ProfileTab.MyDiplomas,

    val diplomas: List<ProfileDiploma> = emptyList(),
    val activeLinks: List<DiplomaShareLink> = emptyList(),
    val history: List<DiplomaLinkHistoryItem> = emptyList(),

    val isShareSheetVisible: Boolean = false,
    val selectedDiplomaIdForSharing: String? = null,
    val selectedDuration: DiplomaShareDuration = DiplomaShareDuration.DAYS_30,
    val generatedLink: DiplomaShareLink? = null,

    val fullscreenQrLink: DiplomaShareLink? = null,
    val message: String? = null
)

enum class ProfileTab {
    MyDiplomas,
    ActiveLinks,
    History
}
