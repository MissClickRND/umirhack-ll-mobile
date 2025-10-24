package bob.colbaskin.hack_template.onboarding.presentation

interface OnBoardingAction {
    data object OnboardingInProgress: OnBoardingAction
    data object OnboardingComplete: OnBoardingAction
}
