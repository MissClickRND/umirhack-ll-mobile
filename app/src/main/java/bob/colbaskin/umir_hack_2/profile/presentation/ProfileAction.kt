package bob.colbaskin.umir_hack_2.profile.presentation

import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareDuration
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareLink

sealed interface ProfileAction {
    data object LoadUser : ProfileAction
    data object Logout : ProfileAction

    data object SelectMyDiplomasTab : ProfileAction
    data object SelectActiveLinksTab : ProfileAction
    data object SelectHistoryTab : ProfileAction

    data class OpenShareSheet(val diplomaId: String) : ProfileAction
    data object CloseShareSheet : ProfileAction

    data class SelectShareDuration(val duration: DiplomaShareDuration) : ProfileAction
    data object GenerateShareLink : ProfileAction
    data object ConfirmShareSheet : ProfileAction

    data class CopyLink(val value: String) : ProfileAction
    data class ShareLink(val value: String) : ProfileAction

    data class ShowQrFullscreen(val link: DiplomaShareLink) : ProfileAction
    data object HideQrFullscreen : ProfileAction

    data class RevokeLink(val linkId: String) : ProfileAction
    data object ClearMessage : ProfileAction
}
