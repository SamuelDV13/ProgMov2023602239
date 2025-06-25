    package com.example.proyect2.preferencias

    import androidx.compose.runtime.staticCompositionLocalOf

    // Declara un CompositionLocal para los colores de la app
    val LocalAppColors = staticCompositionLocalOf<AppColors> {
        // Valor por defecto si no se proporciona, lanza error para evitar uso sin inicializar
        error("No AppColors provided")
    }
