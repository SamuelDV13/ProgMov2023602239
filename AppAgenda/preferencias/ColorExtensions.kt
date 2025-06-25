package com.example.proyect2.preferencias


import androidx.compose.ui.graphics.Color

fun Color.darken(factor: Float = 0.2f): Color = Color(
    red = (red * (1 - factor)).coerceIn(0f, 1f),
    green = (green * (1 - factor)).coerceIn(0f, 1f),
    blue = (blue * (1 - factor)).coerceIn(0f, 1f),
    alpha = alpha
)
