package bob.colbaskin.umir_hack_2.scanner.presentation.utils

import androidx.core.net.toUri

object DiplomaQrTokenParser {

    private val UUID_REGEX =
        Regex("(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")

    private const val QR_HOST = "miss-click.ru"
    private const val QR_SCHEME = "https"

    fun extractTokenOrNull(rawQrText: String?): String? {
        val raw = rawQrText?.trim().orEmpty()
        if (raw.isBlank()) return null

        if (UUID_REGEX.matches(raw)) {
            return raw.lowercase()
        }

        val uri = runCatching { raw.toUri() }.getOrNull() ?: return null

        val scheme = uri.scheme?.lowercase()
        val host = uri.host?.lowercase()

        if (scheme != QR_SCHEME || host != QR_HOST) return null

        val pathToken = uri.lastPathSegment?.trim().orEmpty()
        if (UUID_REGEX.matches(pathToken)) {
            return pathToken.lowercase()
        }

        return null
    }

    fun sanitizeForUi(rawQrText: String?): String? {
        val raw = rawQrText?.trim() ?: return null
        val token = extractTokenOrNull(raw) ?: return raw.take(80)
        return "QR распознан: ${token.take(4)}…"
    }

    fun buildQrUrl(token: String): String {
        val normalized = token.trim().lowercase()
        require(UUID_REGEX.matches(normalized)) { "Invalid QR token format" }
        return "https://$QR_HOST/$normalized"
    }
}
