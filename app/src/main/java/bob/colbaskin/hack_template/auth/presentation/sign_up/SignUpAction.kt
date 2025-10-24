package bob.colbaskin.hack_template.auth.presentation.sign_up

import bob.colbaskin.hack_template.auth.presentation.sign_in.SignInAction

interface SignUpAction {
    data object SignIn : SignUpAction
    data object SignUp : SignUpAction
    data class UpdateEmail(val email: String): SignUpAction
    data class UpdatePassword(val password: String): SignUpAction
}