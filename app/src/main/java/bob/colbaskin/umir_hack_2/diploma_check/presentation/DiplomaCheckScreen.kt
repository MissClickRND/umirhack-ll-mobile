package bob.colbaskin.umir_hack_2.diploma_check.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.diploma_check.presentation.components.QrCheckResultCard
import bob.colbaskin.umir_hack_2.navigation.NavResultKeys
import bob.colbaskin.umir_hack_2.navigation.Screens
import compose.icons.TablerIcons
import compose.icons.tablericons.Loader
import compose.icons.tablericons.Qrcode

@Composable
fun DiplomaCheckScreenRoot(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: DiplomaCheckViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val currentEntry = navController.currentBackStackEntry
    val diplomaEntry = remember(currentEntry) {
        navController.getBackStackEntry(Screens.DiplomaCheck)
    }
    val qrFlow = remember(diplomaEntry) {
        diplomaEntry.savedStateHandle.getStateFlow<String?>(NavResultKeys.QR_TEXT, null)
    }
    val qrText = qrFlow.collectAsState(initial = null).value

    LaunchedEffect(qrText) {
        val value = qrText ?: return@LaunchedEffect
        viewModel.onAction(DiplomaCheckAction.OnQrScanned(value))
        diplomaEntry.savedStateHandle[NavResultKeys.QR_TEXT] = null
    }

    LaunchedEffect(state.infoMessage) {
        state.infoMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.onAction(DiplomaCheckAction.ClearMessage)
        }
    }

    DiplomaCheckScreen(
        state = state,
        onAction = { action ->
            when (action) {
                DiplomaCheckAction.OpenQrScanner -> {
                    navController.navigate(Screens.QrScanner)
                }
//                DiplomaCheckAction.OpenSignIn -> {
//                    navController.navigate(Screens.SignIn)
//                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun DiplomaCheckScreen(
    state: DiplomaCheckState,
    onAction: (DiplomaCheckAction) -> Unit,
) {
    val colors = CustomTheme.colors
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .imePadding()
            .padding(horizontal = 14.dp, vertical = 18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Проверка диплома",
                    color = colors.textPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )

                TextButton(
                    onClick = { onAction(DiplomaCheckAction.OpenSignIn) }
                ) {
                    Text(
                        text = "Войти",
                        color = colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            MainVerificationCard(
                state = state,
                onAction = onAction
            )
            Spacer(modifier = Modifier.height(14.dp))
            QrScannerCard(
                onClick = { onAction(DiplomaCheckAction.OpenQrScanner) }
            )
            Spacer(modifier = Modifier.height(14.dp))
            QrCheckResultCard(
                state = state,
                onScanAgain = { onAction(DiplomaCheckAction.OpenQrScanner) },
                onClear = { onAction(DiplomaCheckAction.ClearQrResult) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun MainVerificationCard(
    state: DiplomaCheckState,
    onAction: (DiplomaCheckAction) -> Unit,
) {
    val colors = CustomTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surface)
            .padding(18.dp)
    ) {
        RegistryBadge()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Проверка\nподлинности",
            color = colors.textPrimary,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Введите данные документа для\nполучения официального подтверждения\nиз ФРДО.",
            color = colors.textSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        SearchInput(
            value = state.query,
            onValueChange = { onAction(DiplomaCheckAction.UpdateQuery(it)) },
            placeholder = "Введите номер диплома или ФИО"
        )

        Spacer(modifier = Modifier.height(14.dp))

        GradientActionButton(
            text = "Проверить",
            isLoading = state.isLoading,
            enabled = state.canVerify || state.isLoading,
            onClick = { onAction(DiplomaCheckAction.VerifyDiploma) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = TablerIcons.Loader,
                contentDescription = null,
                tint = colors.secondary
            )
            Text(
                text = "Проверка происходит мнгновенно!",
                color = colors.textSecondary,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun QrScannerCard(
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surface)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(18.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = TablerIcons.Qrcode,
                contentDescription = null,
                tint = colors.secondary,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Сканировать QR-код",
                color = colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Быстрая проверка по коду на\nбланке диплома без ручного\nввода",
                color = colors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Открыть камеру",
                    color = colors.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colors.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun RegistryBadge() {
    val colors = CustomTheme.colors

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.surfaceSecondary)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(colors.secondary)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = "ОФИЦИАЛЬНЫЙ РЕЕСТР",
            color = colors.textPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.surfaceSecondary)
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = colors.textPrimary,
                fontSize = 14.sp
            ),
            cursorBrush = SolidColor(colors.primary),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun GradientActionButton(
    text: String,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(colors.primary, colors.primaryDark)
                )
            )
            .clickable(
                enabled = enabled && !isLoading,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = colors.textOnPrimary
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = colors.textOnPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = "→",
                    color = colors.textOnPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}