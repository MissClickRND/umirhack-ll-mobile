package bob.colbaskin.umir_hack_2.diploma_check.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.scanner.domain.ScannerRepository
import bob.colbaskin.umir_hack_2.scanner.domain.models.DocumentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository

@HiltViewModel
class DiplomaCheckViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(DiplomaCheckState())
        private set
    private var qrJob: Job? = null

    init {
        observeAuthStatus()
    }

    fun onAction(action: DiplomaCheckAction) {
        when (action) {
            is DiplomaCheckAction.UpdateQuery -> updateQuery(action.value)
            DiplomaCheckAction.VerifyDiploma -> verifyDiploma()
            DiplomaCheckAction.OpenHowItWorks -> openHowItWorksStub()
            DiplomaCheckAction.RefreshAuthStatus -> refreshAuthStatus()
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

    private fun refreshAuthStatus() {
        viewModelScope.launch {
            state = when (val result = authRepository.status()) {
                is ApiResult.Success -> {
                    state.copy(isAuthorized = result.data)
                }

                is ApiResult.Error -> {
                    state.copy(isAuthorized = false)
                }
            }
        }
    }

    private fun observeAuthStatus() {
        viewModelScope.launch {
            when (val result = authRepository.status()) {
                is ApiResult.Success -> {
                    state = state.copy(
                        isAuthorized = result.data
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        isAuthorized = false
                    )
                }
            }
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

    private fun updateQuery(input: TextFieldValue) {
        val rawText = input.text
        val digitsOnly = rawText.filter { it.isDigit() }.take(13)

        val formatted = buildString {
            digitsOnly.forEachIndexed { index, c ->
                if (index == 6) append(' ')
                append(c)
            }
        }

        val digitsBeforeCursor = rawText
            .take(input.selection.start)
            .count { it.isDigit() }
            .coerceAtMost(digitsOnly.length)

        val newCursorPosition = when {
            digitsBeforeCursor <= 6 -> digitsBeforeCursor
            else -> digitsBeforeCursor + 1
        }.coerceAtMost(formatted.length)

        state = state.copy(
            diplomaInput = TextFieldValue(
                text = formatted,
                selection = TextRange(newCursorPosition)
            )
        )
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

    private fun clearMessage() {
        state = state.copy(infoMessage = null)
    }
}
