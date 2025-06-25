package com.example.proyect2.preferencias

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProvideAppColors(
    appColorProvider: AppColorProvider,
    content: @Composable (colors: AppColors) -> Unit
) {
    var principalColor by remember { mutableStateOf(AppColorProvider.defaultColor) }

    LaunchedEffect(appColorProvider) {
        appColorProvider.colorFlow.collectLatest { color ->
            principalColor = color
        }
    }

    val colors = remember(principalColor) {
        AppColors(
            principal = principalColor,
            claro = principalColor.copy(alpha = 0.3f),
            oscuro = principalColor.darken(0.3f),
            texto = principalColor.darken(0.6f)
        )
    }

    content(colors)
}

