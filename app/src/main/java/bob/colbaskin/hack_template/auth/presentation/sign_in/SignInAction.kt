package bob.colbaskin.hack_template.auth.presentation.sign_in

sealed interface SignInAction {
    data object SignIn : SignInAction
    data object SignUp : SignInAction
    data class UpdateEmail(val email: String): SignInAction
    data class UpdatePassword(val password: String): SignInAction
}
