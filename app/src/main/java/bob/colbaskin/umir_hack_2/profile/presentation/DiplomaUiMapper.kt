package bob.colbaskin.umir_hack_2.profile.presentation

import bob.colbaskin.umir_hack_2.profile.domain.models.Diploma
import bob.colbaskin.umir_hack_2.profile.domain.models.ProfileDiploma
import java.time.ZoneId

fun Diploma.toProfileDiplomaUi(): ProfileDiploma {
    val year = issuedAt.atZone(ZoneId.systemDefault()).year

    val title = when (degreeLevel.name) {
        "BACHELOR" -> "Диплом бакалавра"
        "MASTER" -> "Диплом магистра"
        "SPECIALIST" -> "Диплом специалиста"
        else -> "Диплом"
    }

    return ProfileDiploma(
        id = id,
        title = title,
        qualification = specialty,
        year = year,
        number = registrationNumber,
        universityName = university?.shortName ?: (university?.name ?: "Университет"),
        issued = status.name == "VALID"
    )
}
