package com.example.proyect2.datos


import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Asegúrate de que tu clase Tarea esté anotada con @Serializable
// import com.example.proyect2.model.Tarea

object ApiClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
}

object ApiService {
    //private const val BASE_URL = "http://192.168.0.19/api"
    private const val BASE_URL = "http://192.168.198.211/api"

    suspend fun obtenerTareasRemotas(): List<Tarea> {
        return ApiClient.client.get("$BASE_URL/get_tareas.php").body<List<Tarea>>()
    }

    suspend fun subirTarea(tarea: Tarea): Boolean {
        val response: HttpResponse = ApiClient.client.post("$BASE_URL/add_tarea.php") {
            contentType(ContentType.Application.Json)
            setBody(tarea)
        }

        val responseBody = response.bodyAsText()
        Log.d("Sync", "Respuesta del servidor: $responseBody")

        return response.status.isSuccess()
    }


    suspend fun actualizarTarea(tarea: Tarea): Boolean {
        val response: HttpResponse = ApiClient.client.post("$BASE_URL/update_tarea.php") {
            contentType(ContentType.Application.Json)
            setBody(tarea)
        }

        val responseBody = response.bodyAsText()
        Log.d("Sync", "Respuesta del servidor (actualizarTarea): $responseBody")

        return response.status.isSuccess()
    }


    suspend fun eliminarTareaRemota(idGlobal: String): Boolean {
        val response: HttpResponse = ApiClient.client.post("$BASE_URL/delete_tarea.php") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("idGlobal" to idGlobal))
        }

        val responseBody = response.bodyAsText()
        Log.d("Sync", "Respuesta del servidor (eliminarTareaRemota): $responseBody")

        return response.status.isSuccess()
    }





    suspend fun probarConexion(): String {
        return try {
            val response: HttpResponse = ApiClient.client.get("$BASE_URL/get_tareas.php")
            if (response.status.isSuccess()) {
                "Conexión exitosa con el servidor"
            } else {
                "Error: ${response.status}"
            }
        } catch (e: Exception) {
            "Excepción: ${e.message}"
        }
    }
}
