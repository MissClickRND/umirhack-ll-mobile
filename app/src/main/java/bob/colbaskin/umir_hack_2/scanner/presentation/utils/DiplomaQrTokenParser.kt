package bob.colbaskin.umir_hack_2.scanner.presentation.utils

import androidx.core.net.toUri

object DiplomaQrTokenParser {

    private val UUID_REGEX =
        Regex("(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")

    fun extractTokenOrNull(rawQrText: String?): String? {
        val raw = rawQrText?.trim().orEmpty()
        if (raw.isBlank()) return null

        if (UUID_REGEX.matches(raw)) return raw.lowercase()

        val uri = raw.toUri()
        val token = uri.getQueryParameter("token")?.trim().orEmpty()
        if (!UUID_REGEX.matches(token)) return null

        return token.lowercase()
    }

    fun sanitizeForUi(rawQrText: String?): String? {
        val raw = rawQrText?.trim() ?: return null
        val token = extractTokenOrNull(raw) ?: return raw.take(80)
        return "QR распознан (token скрыт): ${token.take(4)}…"
    }
}
