package bob.colbaskin.umir_hack_2.navigation.graphs

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bob.colbaskin.umir_hack_2.auth.presentation.sign_in.SignInScreenRoot
import bob.colbaskin.umir_hack_2.auth.presentation.sign_up.SignUpScreenRoot
import bob.colbaskin.umir_hack_2.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.umir_hack_2.diploma_check.presentation.DiplomaCheckScreenRoot
import bob.colbaskin.umir_hack_2.navigation.Screens
import bob.colbaskin.umir_hack_2.onboarding.presentation.IntroductionScreen
import bob.colbaskin.umir_hack_2.onboarding.presentation.WelcomeScreen
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileScreenRoot
import bob.colbaskin.umir_hack_2.scanner.presentation.camera.ScannerScreenRoot
import bob.colbaskin.umir_hack_2.scanner.presentation.image_selection.ImageAreaSelectionScreenRoot

fun NavGraphBuilder.onboardingGraph(
    navController: NavHostController,
    onboardingStatus: OnboardingConfig,
    snackbarHostState: SnackbarHostState
) {
    navigation<Graphs.Onboarding>(
        startDestination = getStartDestination(onboardingStatus)
    ) {
        composable<Screens.Welcome> {
            WelcomeScreen (
                onNextScreen = { navController.navigate(Screens.Introduction) {
                    popUpTo(Screens.Welcome) { inclusive = true }
                }}
            )
        }
        composable<Screens.Introduction> {
            IntroductionScreen (
                onNextScreen = { navController.navigate(Graphs.Main) {
                    popUpTo(Screens.Introduction) { inclusive = true }
                }}
            )
        }
    }
}

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation<Graphs.Main>(
        startDestination = Screens.DiplomaCheck
    ) {
        composable<Screens.DiplomaCheck> { backStackEntry ->
            DiplomaCheckScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState,
                backStackEntry = backStackEntry
            )
        }
        composable<Screens.Profile> {
            ProfileScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}
fun NavGraphBuilder.detailedGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation<Graphs.Detailed>(
        startDestination = Screens.QrScanner
    ) {
        composable<Screens.QrScanner> {
            ScannerScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable<Screens.ImageAreaSelectionScreen> {
            ImageAreaSelectionScreenRoot(
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation<Graphs.Auth>(
        startDestination = Screens.SignIn
    ) {
        composable<Screens.SignIn> {
            SignInScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable<Screens.SignUp> {
            SignUpScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

private fun getStartDestination(status: OnboardingConfig) = when (status) {
    OnboardingConfig.NOT_STARTED -> Screens.Welcome
    OnboardingConfig.IN_PROGRESS -> Screens.Introduction
    OnboardingConfig.COMPLETED -> Screens.Introduction
    else -> Screens.Welcome
}
