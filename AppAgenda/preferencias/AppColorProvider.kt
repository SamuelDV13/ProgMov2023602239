package com.example.proyect2.preferencias

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class AppColorProvider(private val context: Context) {

    companion object {
        private val COLOR_KEY = intPreferencesKey("color_preference")
        val defaultColor = Color(0xFF1976D2) // Azul predeterminado

        @SuppressLint("StaticFieldLeak")
        private var instance: AppColorProvider? = null
        fun getInstance(context: Context): AppColorProvider {
            return instance ?: AppColorProvider(context.applicationContext).also { instance = it }
        }
    }

    val colorFlow: Flow<Color> = context.dataStore.data.map { prefs ->
        Color(prefs[COLOR_KEY] ?: defaultColor.toArgb())
    }

    suspend fun setColor(color: Color) {
        context.dataStore.edit { prefs ->
            prefs[COLOR_KEY] = color.toArgb()
        }
    }

    // Nueva función para actualizar color a partir de hex string y guardar preferencia
    fun updateColor(colorHex: String) {
        val color = Color(android.graphics.Color.parseColor(colorHex))
        CoroutineScope(Dispatchers.IO).launch {
            setColor(color)
        }
    }

    fun getColor(): Color = runBlocking {
        val prefs = context.dataStore.data.first()
        Color(prefs[COLOR_KEY] ?: defaultColor.toArgb())
    }
}

// Función extensión para convertir Color a Int ARGB
fun Color.toArgb(): Int = android.graphics.Color.argb(
    (alpha * 255).toInt(),
    (red * 255).toInt(),
    (green * 255).toInt(),
    (blue * 255).toInt()
)

