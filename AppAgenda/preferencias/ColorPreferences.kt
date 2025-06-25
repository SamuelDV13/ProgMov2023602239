package com.example.proyect2.preferencias

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStoreQ by preferencesDataStore(name = "color_preferences")

private val COLOR_KEY = stringPreferencesKey("selected_color")

suspend fun saveColorPreference(context: Context, colorHex: String) {
    context.dataStoreQ.edit { preferences ->
        preferences[COLOR_KEY] = colorHex
    }
}

suspend fun getColorPreference(context: Context): String {
    val preferences = context.dataStoreQ.data.first()
    return preferences[COLOR_KEY] ?: "#1976D2"
}
