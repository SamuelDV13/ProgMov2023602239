package com.example.proyect2.navegacion


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyect2.preferencias.AppColorProvider
import com.example.proyect2.vistas.ViewAcercaDe
import com.example.proyect2.vistas.ViewAgregarTareas
import com.example.proyect2.vistas.ViewTareas
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NavigationGraph(context: android.content.Context) {
    val navController = rememberNavController()
    val appColorProvider = remember { AppColorProvider.getInstance(context) }

    val principalColorState = remember { mutableStateOf(AppColorProvider.defaultColor) }

    // Observa el flujo de color de la preferencia y actualiza el estado
    LaunchedEffect(appColorProvider) {
        appColorProvider.colorFlow.collectLatest { color ->
            principalColorState.value = color
        }
    }

    // Función para guardar el color seleccionado en DataStore
    val saveColorPreferenceFunc: suspend (androidx.compose.ui.graphics.Color) -> Unit = { color ->
        appColorProvider.setColor(color)
    }

    NavHost(navController, startDestination = "view_productos") {
        composable("view_productos") {
            ViewTareas(
                navController = navController,
                principalColorState = principalColorState,
                saveColorPreferenceFunc = saveColorPreferenceFunc
            )
        }

        composable("view_agregarProducto/{idP}") { backStackEntry ->
            ViewAgregarTareas(navController, backStackEntry.arguments?.getString("idP"))
        }

        composable("view_acercaDe") { ViewAcercaDe(navController) }
    }
}

