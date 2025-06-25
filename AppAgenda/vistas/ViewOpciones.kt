package com.example.proyect2.vistas

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun BotonConMenu() {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded = true }) {
            Text("Mostrar ventana")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Hola") },
                onClick = { expanded = false }
            )
        }
    }
}
