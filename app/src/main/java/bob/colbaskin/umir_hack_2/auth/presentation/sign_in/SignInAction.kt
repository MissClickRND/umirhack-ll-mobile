package bob.colbaskin.umir_hack_2.auth.presentation.sign_in

sealed interface SignInAction {
    data object SignIn : SignInAction
    data object SignUp : SignInAction
    data class UpdateEmail(val email: String): SignInAction
    data class UpdatePassword(val password: String): SignInAction
}
