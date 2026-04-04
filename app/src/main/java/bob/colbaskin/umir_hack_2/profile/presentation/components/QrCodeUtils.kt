package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

fun generateQrImageBitmap(
    content: String,
    size: Int = 800
): ImageBitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(
        content,
        BarcodeFormat.QR_CODE,
        size,
        size
    )

    val bitmap = createBitmap(size, size)

    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap[x, y] =
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }

    return bitmap.asImageBitmap()
}
