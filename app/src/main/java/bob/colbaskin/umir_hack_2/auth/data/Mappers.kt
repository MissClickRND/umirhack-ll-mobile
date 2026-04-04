package bob.colbaskin.umir_hack_2.auth.data

import bob.colbaskin.umir_hack_2.auth.data.models.dto.UserDTO
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.Role
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import java.time.Instant

fun UserDTO.toDomain(): User {
    return User(
        id = id,
        email = email,
        role = role.toDomain(),
    )
}

fun User.toData(): UserDTO {
    return UserDTO(
        id = id,
        email = email,
        role = role.toData(),
    )
}

fun Role.toData(): String = when (this) {
    Role.STUDENT -> "student"
    Role.GUEST -> "guest"
}

fun String.toDomain(): Role = when (this.lowercase()) {
    "student" -> Role.STUDENT
    else -> Role.GUEST
}
