package com.example.holatoast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UIPrincipal() }
    }
}

@Composable
fun UIPrincipal() {

    var cadTxtNombre by rememberSaveable { mutableStateOf("") }
    var cadLblMensaje by rememberSaveable { mutableStateOf("") }

    fun btnSaludar_click(){
        cadLblMensaje = "Hola $cadTxtNombre"
    }

    Column(Modifier.fillMaxSize().padding(16.dp), Arrangement.Top, Alignment.Start){
        Text("¿Como te llamas?")
        TextField(
            value = cadTxtNombre,
            onValueChange = {cadTxtNombre = it}
        )
        Button(onClick = { btnSaludar_click() }) {
            Text("Saludar")
        }
        Text(cadLblMensaje)
    }

}

@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    UIPrincipal()
}