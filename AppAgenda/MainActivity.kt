package com.example.proyect2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.example.proyect2.navegacion.NavigationGraph
import com.example.proyect2.preferencias.AppColorProvider
import com.example.proyect2.preferencias.LocalAppColors
import com.example.proyect2.preferencias.ProvideAppColors

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appColorProvider = AppColorProvider.getInstance(this)

        setContent {
            ProvideAppColors(appColorProvider) { colors ->
                CompositionLocalProvider(LocalAppColors provides colors) {
                    NavigationGraph(context = this)
                }
            }
        }
    }
}
