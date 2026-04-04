package bob.colbaskin.umir_hack_2.onboarding.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bob.colbaskin.umir_hack_2.common.design_system.theme.LocalColors

@Composable
fun WelcomeScreen(
    onNextScreen: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    Welcome(onNextScreen, viewModel::action)
}

@Composable
fun Welcome(
    onNextScreen: () -> Unit,
    dispatch: (OnBoardingAction) -> Unit
) {
    val colors = LocalColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.background,
                        colors.surface
                    )
                )
            )
            .padding(
                top = 24.dp,
                bottom = 32.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .background(
                            color = colors.buttonSecondaryBackground,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colors.outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Платформа дипломов",
                        color = colors.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Открытая проверка подлинности диплома",
                    color = colors.textPrimary,
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Платформа позволяет каждому пользователю самостоятельно проверить подлинность своего диплома. Данные хранятся в защищенном виде, а для проверки достаточно ввести номер диплома и фамилию владельца.",
                    color = colors.textSecondary,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(colors.primary, colors.primaryDark)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Что умеет наше решение?",
                            color = colors.textOnPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "• безопасная загрузка данных диплома\n" +
                                    "• шифрование хранимых записей\n" +
                                    "• быстрый сценарий проверки подлинности диплома\n" +
                                    "• обмен захэшированным токеном диплома по ссылке или QR-коду",
                            color = colors.textOnPrimary,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.textOnPrimary
                    ),
                    shape = RoundedCornerShape(18.dp),
                    onClick = {
                        dispatch(OnBoardingAction.OnboardingInProgress)
                        onNextScreen()
                    }
                ) {
                    Text(
                        text = "Начать",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
