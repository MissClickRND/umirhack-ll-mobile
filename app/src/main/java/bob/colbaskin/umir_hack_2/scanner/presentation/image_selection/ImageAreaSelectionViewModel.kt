package bob.colbaskin.umir_hack_2.scanner.presentation.image_selection

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class ImageAreaSelectionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var state by mutableStateOf(ImageAreaSelectionState())
        private set

    private val barcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    fun onAction(action: ImageAreaSelectionAction) {
        when (action) {
            is ImageAreaSelectionAction.SetImageUri -> {
                state = state.copy(
                    imageUri = action.uri,
                    isLoading = false,
                    detectedQrText = null,
                    errorMessage = null,
                    isCompleted = false
                )
            }

            ImageAreaSelectionAction.StartDetection -> detectAndCheck()
            ImageAreaSelectionAction.RetryDetection -> detectAndCheck()

            ImageAreaSelectionAction.ClearError -> {
                state = state.copy(errorMessage = null)
            }

            ImageAreaSelectionAction.Cancel -> Unit
        }
    }

    private fun detectAndCheck() {
        val imageUriString = state.imageUri ?: return

        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                errorMessage = null,
                detectedQrText = null,
                isCompleted = false
            )

            val qrText: String? = withContext(Dispatchers.IO) {
                runCatching {
                    val uri = imageUriString.toUri()
                    val bitmap = loadBitmapFromUri(context.contentResolver, uri) ?: return@runCatching null
                    val image = InputImage.fromBitmap(bitmap, 0)
                    val barcodes = barcodeScanner.process(image).await()
                    barcodes.firstOrNull()?.rawValue?.trim()
                }.getOrNull()
            }

            if (qrText.isNullOrBlank()) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = "QR-код на изображении не найден. Попробуй выбрать более четкое фото."
                )
                return@launch
            }

            val normalized = normalizeQrText(qrText)

            if (normalized.isBlank()) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = "Не удалось корректно прочитать QR-код."
                )
                return@launch
            }

            state = state.copy(
                isLoading = false,
                detectedQrText = normalized,
                isCompleted = true
            )
        }
    }

    private fun loadBitmapFromUri(
        contentResolver: ContentResolver,
        uri: Uri
    ): Bitmap? {
        return contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input)
        }
    }

    private fun normalizeQrText(raw: String): String {
        val value = raw.trim()
        if (value.isBlank()) return ""

        return if (value.startsWith("http://") || value.startsWith("https://")) {
            value.toUri().lastPathSegment ?: value
        } else {
            value
        }
    }

    override fun onCleared() {
        super.onCleared()
        barcodeScanner.close()
    }
}
