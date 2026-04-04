package bob.colbaskin.umir_hack_2.onboarding.presentation

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bob.colbaskin.umir_hack_2.R
import bob.colbaskin.umir_hack_2.common.design_system.Lottie
import bob.colbaskin.umir_hack_2.common.design_system.theme.LocalColors
import kotlinx.coroutines.launch

@Composable
fun IntroductionScreen(
    onNextScreen: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    OnBoarding(onNextScreen, viewModel::action)
}

@Composable
private fun OnBoarding(
    onNextScreen: () -> Unit,
    dispatch: (OnBoardingAction) -> Unit
) {
    val colors = LocalColors.current
    val coroutineScope = rememberCoroutineScope()
    val pageCount = OnBoardingPage.allPages.size

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { pageCount }
    )

    val buttonText = if (pagerState.currentPage == pageCount - 1) "Начать работу" else "Дальше"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colors.background, colors.surface)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Как это работает",
                color = colors.textSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Онбординг платформы",
                color = colors.textPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            PagerWithIndicator(
                pageCount = pageCount,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 24.dp),
                pagerState = pagerState
            ) { position ->
                val page = OnBoardingPage.allPages[position]

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = colors.surfaceSecondary,
                                shape = RoundedCornerShape(32.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = colors.outline,
                                shape = RoundedCornerShape(32.dp)
                            )
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Lottie(
                            lottieJson = page.lottieJson,
                            modifier = Modifier.size(170.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = page.title,
                        color = colors.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = page.description,
                        color = colors.textSecondary,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < pageCount - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            dispatch(OnBoardingAction.OnboardingComplete)
                            onNextScreen()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.textOnPrimary
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    dispatch(OnBoardingAction.OnboardingComplete)
                    onNextScreen()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.textSecondary
                )
            ) {
                Text(
                    text = "Пропустить",
                    fontSize = 15.sp
                )
            }
        }
    }
}

private sealed class OnBoardingPage(
    @param:RawRes val lottieJson: Int,
    val title: String,
    val description: String
) {
    data object First : OnBoardingPage(
        lottieJson = R.raw.first,
        title = "ВУЗ загружает диплом",
        description = "Учебное заведение передаёт сведения о выпускнике и дипломе на платформу-посредник. После загрузки запись подготавливается к безопасному хранению и дальнейшей верификации."
    )

    data object Second : OnBoardingPage(
        lottieJson = R.raw.second,
        title = "Данные шифруются",
        description = "Каждый вуз использует собственный набор криптографических ключей для шифрования данных дипломов. Без нужных ключей расшифровать и получить эти данные невозможно, поэтому информация остается защищенной."
    )

    data object Third : OnBoardingPage(
        lottieJson = R.raw.third,
        title = "Открытая проверка диплома",
        description = "Платформа открыта для всех: любой пользователь может проверить подлинность своего диплома, введя его номер и свою фамилию. Система быстро найдет сертификат и подтвердит его валидность."
    )

    companion object {
        val allPages = listOf(First, Second, Third)
    }
}
