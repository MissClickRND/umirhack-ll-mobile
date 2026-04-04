package bob.colbaskin.umir_hack_2.scanner.presentation.image_selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.navigation.NavResultKeys
import bob.colbaskin.umir_hack_2.navigation.Screens
import coil.compose.AsyncImage
import coil.request.ImageRequest
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Photo
import compose.icons.tablericons.Refresh

@Composable
fun ImageAreaSelectionScreenRoot(
    navController: NavHostController,
    viewModel: ImageAreaSelectionViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val imageUri = navController.currentBackStackEntry?.arguments?.getString("imageUri")

    LaunchedEffect(imageUri) {
        if (imageUri == null) {
            navController.popBackStack()
            return@LaunchedEffect
        }

        viewModel.onAction(ImageAreaSelectionAction.SetImageUri(imageUri))
        viewModel.onAction(ImageAreaSelectionAction.StartDetection)
    }

    LaunchedEffect(state.isCompleted, state.detectedQrText) {
        val qrText = state.detectedQrText ?: return@LaunchedEffect
        if (state.isCompleted) {
            val diplomaEntry = navController.getBackStackEntry(Screens.DiplomaCheck)
            diplomaEntry.savedStateHandle[NavResultKeys.QR_TEXT] = qrText
            navController.popBackStack(Screens.DiplomaCheck, false)
        }
    }

    ImageAreaSelectionScreen(
        state = state,
        onAction = { action ->
            when (action) {
                ImageAreaSelectionAction.Cancel -> navController.popBackStack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun ImageAreaSelectionScreen(
    state: ImageAreaSelectionState,
    onAction: (ImageAreaSelectionAction) -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    onClick = { onAction(ImageAreaSelectionAction.Cancel) },
                    shape = CircleShape,
                    color = colors.buttonSecondaryBackground
                ) {
                    Box(
                        modifier = Modifier.size(44.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = TablerIcons.ArrowLeft,
                            contentDescription = "Назад",
                            tint = colors.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = "Проверка изображения",
                        color = colors.textPrimary,
                        style = CustomTheme.typography.inter.titleLarge
                    )
                    Text(
                        text = "Найдем QR-код на фото и проверим документ",
                        color = colors.textSecondary,
                        style = CustomTheme.typography.inter.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colors.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(colors.surfaceSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.imageUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(state.imageUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Выбранное изображение",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = TablerIcons.Photo,
                                    contentDescription = null,
                                    tint = colors.textSecondary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(
                                    text = "Изображение не выбрано",
                                    color = colors.textSecondary
                                )
                            }
                        }

                        if (state.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(colors.background.copy(alpha = 0.56f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(color = colors.primary)
                                    Spacer(modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Ищем QR-код и проверяем документ...",
                                        color = colors.textPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    when {
                        state.isLoading -> {
                            Text(
                                text = "Это займет пару секунд",
                                color = colors.textSecondary,
                                style = CustomTheme.typography.inter.bodyMedium
                            )
                        }

                        !state.errorMessage.isNullOrBlank() -> {
                            Text(
                                text = state.errorMessage,
                                color = colors.textPrimary,
                                style = CustomTheme.typography.inter.bodyMedium
                            )

                            Spacer(modifier = Modifier.size(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = { onAction(ImageAreaSelectionAction.RetryDetection) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colors.primary,
                                        contentColor = colors.textOnPrimary
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(
                                        imageVector = TablerIcons.Refresh,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text("Повторить")
                                }

                                TextButton(
                                    onClick = { onAction(ImageAreaSelectionAction.Cancel) }
                                ) {
                                    Text("Назад", color = colors.textSecondary)
                                }
                            }
                        }

                        !state.detectedQrText.isNullOrBlank() -> {
                            Text(
                                text = "QR найден: ${state.detectedQrText}",
                                color = colors.textPrimary,
                                style = CustomTheme.typography.inter.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
