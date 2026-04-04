package bob.colbaskin.umir_hack_2.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository
import bob.colbaskin.umir_hack_2.common.toUiState
import bob.colbaskin.umir_hack_2.profile.domain.ProfileRepository
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaLinkHistoryItem
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareDuration
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareLink
import bob.colbaskin.umir_hack_2.profile.domain.models.ProfileDiploma
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    init {
        loadUser()
        seedLocalData()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.LoadUser -> loadUser()
            ProfileAction.Logout -> logout()

            ProfileAction.SelectMyDiplomasTab -> {
                state = state.copy(selectedTab = ProfileTab.MyDiplomas)
            }

            ProfileAction.SelectActiveLinksTab -> {
                state = state.copy(selectedTab = ProfileTab.ActiveLinks)
            }

            ProfileAction.SelectHistoryTab -> {
                state = state.copy(selectedTab = ProfileTab.History)
            }

            is ProfileAction.OpenShareSheet -> {
                state = state.copy(
                    isShareSheetVisible = true,
                    selectedDiplomaIdForSharing = action.diplomaId,
                    selectedDuration = DiplomaShareDuration.DAYS_30,
                    generatedLink = generateTemporaryLink(
                        diplomaId = action.diplomaId,
                        duration = DiplomaShareDuration.DAYS_30
                    )
                )
            }

            ProfileAction.CloseShareSheet -> {
                state = state.copy(
                    isShareSheetVisible = false,
                    selectedDiplomaIdForSharing = null,
                    generatedLink = null
                )
            }

            is ProfileAction.SelectShareDuration -> {
                val diplomaId = state.selectedDiplomaIdForSharing ?: return
                state = state.copy(
                    selectedDuration = action.duration,
                    generatedLink = generateTemporaryLink(
                        diplomaId = diplomaId,
                        duration = action.duration
                    )
                )
            }

            ProfileAction.GenerateShareLink -> {
                val diplomaId = state.selectedDiplomaIdForSharing ?: return
                state = state.copy(
                    generatedLink = generateTemporaryLink(
                        diplomaId = diplomaId,
                        duration = state.selectedDuration
                    )
                )
            }

            ProfileAction.ConfirmShareSheet -> confirmShareLink()

            is ProfileAction.CopyLink -> {
                state = state.copy(message = "Ссылка готова к копированию")
            }

            is ProfileAction.ShareLink -> {
                state = state.copy(message = "Открой share intent позже")
            }

            is ProfileAction.ShowQrFullscreen -> {
                state = state.copy(fullscreenQrLink = action.link)
            }

            ProfileAction.HideQrFullscreen -> {
                state = state.copy(fullscreenQrLink = null)
            }

            is ProfileAction.RevokeLink -> revokeLink(action.linkId)

            ProfileAction.ClearMessage -> {
                state = state.copy(message = null)
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = profileRepository.getUser()
            state = state.copy(userState = user.toUiState())
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    private fun seedLocalData() {
        val now = Instant.now()

        val diploma1 = ProfileDiploma(
            id = "diploma_1",
            title = "Диплом бакалавра",
            qualification = "Информатика и вычислительная техника",
            year = 2024,
            number = "107724 5589012",
            universityName = "МГТУ им. Н.Э. Баумана",
            issued = true
        )

        val diploma2 = ProfileDiploma(
            id = "diploma_2",
            title = "Диплом магистра",
            qualification = "Программная инженерия",
            year = 2026,
            number = "207811 1145321",
            universityName = "НИУ ВШЭ",
            issued = true
        )

        val activeLink1 = DiplomaShareLink(
            id = "link_1",
            diplomaId = "diploma_1",
            token = "8x9Q2m",
            shortUrl = "edu.ru/v/8x9Q2m",
            createdAt = now.minusSeconds(15 * 60),
            expiresAt = now.plusSeconds(23 * 60 * 60 + 45 * 60),
            isRevoked = false
        )

        val activeLink2 = DiplomaShareLink(
            id = "link_2",
            diplomaId = "diploma_2",
            token = "pL4k9N",
            shortUrl = "edu.ru/v/pL4k9N",
            createdAt = now.minusSeconds(24 * 60 * 60 + 5 * 60),
            expiresAt = now.plusSeconds(6 * 24 * 60 * 60),
            isRevoked = false
        )

        val history1 = DiplomaLinkHistoryItem(
            id = "history_1",
            diplomaId = "diploma_1",
            diplomaTitle = "Диплом бакалавра",
            shortUrl = "edu.ru/v/mN2b8V",
            createdAt = now.minusSeconds(12 * 24 * 60 * 60),
            expiresAt = now.minusSeconds(7 * 24 * 60 * 60),
            wasRevoked = false
        )

        val history2 = DiplomaLinkHistoryItem(
            id = "history_2",
            diplomaId = "diploma_2",
            diplomaTitle = "Диплом магистра",
            shortUrl = "edu.ru/v/yX7z9P",
            createdAt = now.minusSeconds(9 * 24 * 60 * 60),
            expiresAt = now.minusSeconds(3 * 24 * 60 * 60),
            wasRevoked = true
        )

        state = state.copy(
            diplomas = listOf(diploma1, diploma2),
            activeLinks = listOf(activeLink1, activeLink2),
            history = listOf(history1, history2)
        )
    }

    private fun confirmShareLink() {
        val link = state.generatedLink ?: return

        state = state.copy(
            activeLinks = listOf(link) + state.activeLinks.filterNot { it.id == link.id },
            history = listOf(
                DiplomaLinkHistoryItem(
                    id = UUID.randomUUID().toString(),
                    diplomaId = link.diplomaId,
                    diplomaTitle = state.diplomas.firstOrNull { it.id == link.diplomaId }?.title ?: "Диплом",
                    shortUrl = link.shortUrl,
                    createdAt = link.createdAt,
                    expiresAt = link.expiresAt,
                    wasRevoked = false
                )
            ) + state.history,
            selectedTab = ProfileTab.ActiveLinks,
            isShareSheetVisible = false,
            selectedDiplomaIdForSharing = null,
            generatedLink = null,
            message = "Ссылка создана"
        )
    }

    private fun revokeLink(linkId: String) {
        val link = state.activeLinks.firstOrNull { it.id == linkId } ?: return

        state = state.copy(
            activeLinks = state.activeLinks.filterNot { it.id == linkId },
            history = listOf(
                DiplomaLinkHistoryItem(
                    id = UUID.randomUUID().toString(),
                    diplomaId = link.diplomaId,
                    diplomaTitle = state.diplomas.firstOrNull { it.id == link.diplomaId }?.title ?: "Диплом",
                    shortUrl = link.shortUrl,
                    createdAt = link.createdAt,
                    expiresAt = link.expiresAt,
                    wasRevoked = true
                )
            ) + state.history,
            message = "Ссылка отозвана"
        )
    }

    private fun generateTemporaryLink(
        diplomaId: String,
        duration: DiplomaShareDuration
    ): DiplomaShareLink {
        val createdAt = Instant.now()
        val expiresAt = createdAt.plusSeconds(duration.hours * 60 * 60)
        val token = UUID.randomUUID().toString().replace("-", "").take(8)

        return DiplomaShareLink(
            id = UUID.randomUUID().toString(),
            diplomaId = diplomaId,
            token = token,
            shortUrl = "edu.ru/v/$token",
            createdAt = createdAt,
            expiresAt = expiresAt
        )
    }
}
