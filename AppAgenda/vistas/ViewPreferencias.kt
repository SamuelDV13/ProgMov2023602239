package com.example.proyect2.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ViewAcercaDe(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White)
        ) {
            Text("Integrantes", style = MaterialTheme.typography.headlineSmall, color = Color.Blue)
            Divider()
            Text("• Domínguez Virelas Samuel")
            Text("• Cano Zamudio Daniel")
            Text("• Ortega Castillo Juan Felipe")
            Text("• Hernandez Sanchez Axel David")
            Text("Secuencia: 6NM60")
        }
    }
}
