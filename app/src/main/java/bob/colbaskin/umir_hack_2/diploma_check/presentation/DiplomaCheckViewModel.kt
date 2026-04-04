package bob.colbaskin.umir_hack_2.diploma_check.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.umir_hack_2.scanner.domain.ScannerRepository
import bob.colbaskin.umir_hack_2.scanner.domain.models.DocumentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri

@HiltViewModel
class DiplomaCheckViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    var state by mutableStateOf(DiplomaCheckState())
        private set
    private var qrJob: Job? = null

    fun onAction(action: DiplomaCheckAction) {
        when (action) {
            is DiplomaCheckAction.UpdateQuery -> updateQuery(action.value)
            DiplomaCheckAction.VerifyDiploma -> verifyDiploma()
            DiplomaCheckAction.OpenHowItWorks -> openHowItWorksStub()
            DiplomaCheckAction.OpenSignIn -> openSignInStub()
            DiplomaCheckAction.ClearMessage -> clearMessage()
            is DiplomaCheckAction.OnQrScanned -> handleQr(action.qrText)
            DiplomaCheckAction.ClearQrResult -> state = state.copy(
                qrCheckLoading = false,
                qrStatus = DocumentStatus.NOT_SCANNED,
                qrError = null,
                qrRawText = null
            )
            else -> Unit
        }
    }

    private fun handleQr(qrText: String) {
        val normalized = normalizeQrToBackendKey(qrText)
        if (normalized.isBlank()) {
            state = state.copy(
                qrCheckLoading = false,
                qrStatus = DocumentStatus.NOT_SCANNED,
                qrError = "QR-код не распознан или имеет неверный формат",
                qrRawText = null
            )
            return
        }

        qrJob?.cancel()
        qrJob = viewModelScope.launch {
            state = state.copy(
                qrCheckLoading = true,
                qrStatus = DocumentStatus.NOT_SCANNED,
                qrError = null,
                qrRawText = qrText
            )

            when (val result = scannerRepository.checkDocument(normalized)) {
                is ApiResult.Success -> {
                    val status = when (result.data.status.lowercase()) {
                        "green" -> DocumentStatus.GREEN
                        "red" -> DocumentStatus.RED
                        else -> DocumentStatus.NOT_SCANNED
                    }

                    state = state.copy(
                        qrCheckLoading = false,
                        qrStatus = status,
                        qrError = null
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        qrCheckLoading = false,
                        qrStatus = DocumentStatus.RED,
                        qrError = result.title.ifBlank { "Ошибка проверки документа" }
                    )
                }
            }
        }
    }

    private fun normalizeQrToBackendKey(qrText: String): String {
        val t = qrText.trim()
        if (t.isBlank()) return ""

        return if (t.startsWith("http://") || t.startsWith("https://")) {
            runCatching { t.toUri().lastPathSegment ?: t }.getOrDefault(t)
        } else t
    }

    private fun updateQuery(value: String) {
        state = state.copy(query = value)
    }

    private fun verifyDiploma() {
        if (!state.canVerify) return

        viewModelScope.launch {
            state = state.copy(isLoading = true)

            delay(900)

            state = state.copy(
                isLoading = false,
                infoMessage = "Заглушка: проверка диплома будет подключена после интеграции с бэкендом."
            )
        }
    }

    private fun openHowItWorksStub() {
        state = state.copy(
            infoMessage = "Заглушка: здесь можно открыть экран с описанием процесса проверки."
        )
    }

    private fun openSignInStub() {
        viewModelScope.launch {
            userPreferencesRepository.saveAuthStatus(AuthConfig.AUTHENTICATED)
        }
        state = state.copy(
            infoMessage = "Переход на экран авторизации."
        )
    }

    private fun clearMessage() {
        state = state.copy(infoMessage = null)
    }
}
