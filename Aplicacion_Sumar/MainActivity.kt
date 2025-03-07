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

    var cadTxtOp1 by remember { mutableStateOf("") }
    var cadTxtOp2 by remember { mutableStateOf("") }
    var cadTxtRes by remember { mutableStateOf("") }

    fun btnLimpiar_click(){
        cadTxtOp1 = ""
        cadTxtOp2 = ""
        cadTxtRes = ""
    }

    fun btnSumar_click(){
        val op1 = cadTxtOp1.toIntOrNull() ?: 0
        val op2 = cadTxtOp2.toIntOrNull() ?: 0
        cadTxtRes = (op1 + op2).toString()
    }

    Column (Modifier.fillMaxSize().padding(16.dp), Arrangement.Top, Alignment.CenterHorizontally) {
        Row (Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceEvenly, Alignment.CenterVertically){
            Text("Op1", Modifier.weight(1f).padding(start = 15.dp, end = 40.dp))
            Text("Op2", Modifier.weight(1f).padding(start = 15.dp, end = 40.dp))
            Text("Res", Modifier.weight(1f).padding(start = 15.dp, end = 40.dp))
        }

        Row (Modifier.fillMaxWidth().padding(12.dp), Arrangement.SpaceAround){
            TextField(value = cadTxtOp1, onValueChange = {cadTxtOp1 = it}, Modifier.weight(2f).padding(5.dp), singleLine = true)
            Text(" + ", Modifier.weight(0.5f).padding(3.dp).align(Alignment.CenterVertically), fontSize = 20.sp)
            TextField(value = cadTxtOp2, onValueChange = {cadTxtOp2 = it}, Modifier.weight(2f).padding(5.dp), singleLine = true)
            Text(" = ", Modifier.weight(0.5f).padding(3.dp).align(Alignment.CenterVertically), fontSize = 20.sp)
            TextField(value = cadTxtRes, onValueChange = {cadTxtRes = it}, Modifier.weight(2f).padding(5.dp), singleLine = true, readOnly = true)
        }

        Row (Modifier.fillMaxWidth().padding(12.dp), Arrangement.SpaceAround){
            Button(onClick = {btnLimpiar_click()}, Modifier.weight(1.5f).padding(5.dp)) {
                Text("Limpiar")
            }
            Button(onClick = {btnSumar_click()}, Modifier.weight(1.5f).padding(5.dp)) {
                Text("Sumar")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    UIPrincipal()
}