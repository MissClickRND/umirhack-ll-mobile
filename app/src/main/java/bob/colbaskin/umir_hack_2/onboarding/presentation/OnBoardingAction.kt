package bob.colbaskin.umir_hack_2.onboarding.presentation

interface OnBoardingAction {
    data object OnboardingInProgress: OnBoardingAction
    data object OnboardingComplete: OnBoardingAction
}
