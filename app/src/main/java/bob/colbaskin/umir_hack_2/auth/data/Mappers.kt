package bob.colbaskin.umir_hack_2.auth.data

import bob.colbaskin.umir_hack_2.auth.data.models.UserDTO
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import java.time.Instant

fun UserDTO.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        role = role,
        createdAt = Instant.parse(createdAt)
    )
}

fun User.toData(): UserDTO {
    return UserDTO(
        id = id,
        email = email,
        name = name,
        role = role,
        createdAt = createdAt.toString()
    )
}
