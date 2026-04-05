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
import kotlinx.coroutines.launch
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository
import bob.colbaskin.umir_hack_2.common.UiState
import bob.colbaskin.umir_hack_2.common.toUiState
import bob.colbaskin.umir_hack_2.profile.domain.ProfileRepository
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaStatus
import bob.colbaskin.umir_hack_2.scanner.presentation.utils.DiplomaQrTokenParser

@HiltViewModel
class DiplomaCheckViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    var state by mutableStateOf(DiplomaCheckState())
        private set
    private var qrJob: Job? = null

    init {
        refreshAuthStatus()
    }

    fun onAction(action: DiplomaCheckAction) {
        when (action) {
            is DiplomaCheckAction.UpdateQuery -> updateQuery(action.value)
            DiplomaCheckAction.VerifyDiploma -> verifyDiploma()
            DiplomaCheckAction.RefreshAuthStatus -> refreshAuthStatus()
            DiplomaCheckAction.ClearMessage -> clearMessage()
            is DiplomaCheckAction.OnQrScanned -> onQrScanned(action.raw)
            DiplomaCheckAction.ClearQrResult -> {
                qrJob?.cancel()
                state = state.copy(
                    qrCheckLoading = false,
                    qrRawText = null,
                    qrError = null,
                    numDiploma = null,
                    qrStatus = DocumentStatus.NOT_SCANNED
                )
            }
            is DiplomaCheckAction.UpdateFullName -> {
                state = state.copy(fullNameInput = action.value)
            }
            else -> Unit
        }
    }

    private fun refreshAuthStatus() {
        viewModelScope.launch {
            val authorized = resolveAuthStatus()
            state = state.copy(isAuthorized = authorized)
        }
    }

    private suspend fun resolveAuthStatus(): Boolean {
        val firstCheck = authRepository.status()
        if (firstCheck is ApiResult.Success) return firstCheck.data

        val refreshResult = authRepository.refresh()
        if (refreshResult is ApiResult.Error) return false

        return when (val secondCheck = authRepository.status()) {
            is ApiResult.Success -> secondCheck.data
            is ApiResult.Error -> false
        }
    }

    private fun onQrScanned(raw: String) {
        val token = DiplomaQrTokenParser.extractTokenOrNull(raw)

        state = state.copy(
            qrCheckLoading = true,
            qrError = null,
            numDiploma = null,
            qrRawText = DiplomaQrTokenParser.sanitizeForUi(raw),
            qrStatus = DocumentStatus.NOT_SCANNED
        )

        if (token == null) {
            state = state.copy(
                qrCheckLoading = false,
                qrError = "Некорректный QR-код: token не найден.",
                qrStatus = DocumentStatus.RED
            )
            return
        }

        qrJob?.cancel()
        qrJob = viewModelScope.launch {
            when (val result = profileRepository.getDiplomaByQrToken(token)) {
                is ApiResult.Success -> {
                    val diploma = result.data

                    val status = when (diploma.status) {
                        DiplomaStatus.VALID -> DocumentStatus.GREEN
                        DiplomaStatus.REVOKED -> DocumentStatus.RED
                        DiplomaStatus.UNKNOWN -> DocumentStatus.RED
                    }

                    state = state.copy(
                        qrCheckLoading = false,
                        numDiploma = diploma,
                        qrError = null,
                        qrStatus = status
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        qrCheckLoading = false,
                        numDiploma = null,
                        qrError = result.text,
                        qrStatus = DocumentStatus.RED
                    )
                }
            }
        }
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
            state = state.copy(
                isLoading = true,
                qrCheckLoading = true,
                qrError = null,
                qrStatus = DocumentStatus.NOT_SCANNED,
                numDiploma = null,
                diplomaResult = UiState.Loading
            )

            val diplomaNumber = state.diplomaInput.text.replace(" ", "")
            val result = profileRepository.searchDiplomaByNumber(
                number = diplomaNumber,
                fullName = state.fullName
            ).toUiState()

            state = when (result) {
                is UiState.Success -> state.copy(
                    isLoading = false,
                    qrCheckLoading = false,
                    qrStatus = DocumentStatus.GREEN,
                    qrError = null,
                    numDiploma = result.data,
                    diplomaResult = result,
                    infoMessage = "Диплом найден"
                )

                is UiState.Error -> state.copy(
                    isLoading = false,
                    qrCheckLoading = false,
                    qrStatus = DocumentStatus.RED,
                    qrError = result.text,
                    numDiploma = null,
                    diplomaResult = result,
                    infoMessage = "Ошибка проверки: ${result.text}"
                )

                UiState.Loading -> state.copy(
                    isLoading = false,
                    qrCheckLoading = false,
                    diplomaResult = result
                )
            }
        }
    }

    private fun clearMessage() {
        state = state.copy(infoMessage = null)
    }
}
