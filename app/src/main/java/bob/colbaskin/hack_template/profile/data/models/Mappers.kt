package bob.colbaskin.hack_template.profile.data.models

import bob.colbaskin.hack_template.profile.domain.models.User

fun UserDTO.toDomain(): User {
    return User(
        userId = this.id,
        email = this.email
    )
}
