package bob.colbaskin.umir_hack_2.profile.domain.models

enum class DiplomaShareDuration(
    val label: String,
    val hours: Long
) {
    HOURS_12("12 ч", 12),
    HOURS_24("24 ч", 24),
    HOURS_72("72 ч", 72),
    DAYS_7("7 дней", 24 * 7),
    DAYS_30("30 дней", 24 * 30)
}
