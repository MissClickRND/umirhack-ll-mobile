package bob.colbaskin.umir_hack_2.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft

@Composable
fun AuthScreenContainer(
    title: String,
    subtitle: String,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    onBackClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = CustomTheme.colors
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colors.background,
                        colors.surface
                    )
                )
            )
            .statusBarsPadding()
            .imePadding()
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart),
            onClick = onBackClick,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = colors.textSecondary,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colors.surfaceSecondary,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colors.outline,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    }
}
