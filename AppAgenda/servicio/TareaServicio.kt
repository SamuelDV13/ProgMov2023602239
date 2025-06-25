
/*package com.example.proyect2.servicio

import android.content.Context
import com.example.proyect2.datos.DBHelper
import com.example.proyect2.datos.RetrofitClient
import com.example.proyect2.datos.Tarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TareaServicio(private val contexto: Context) {
    private val db = DBHelper(contexto)
    private val apiService = RetrofitClient.instance // Instancia del ApiService

    // Sincroniza la base de datos local con la remota
    suspend fun sincronizarTareas(): String {
        val tareasLocal = obtenerTareasLocales() // Obtener las tareas locales
        val tareasRemotas = apiService.getTareas() // Obtener las tareas remotas

        // Listas para los resultados
        val tareasAActualizar = mutableListOf<Tarea>()
        val tareasAInsertar = mutableListOf<Tarea>()
        val tareasAEliminar = mutableListOf<Tarea>()

        // Comparar tareas locales con tareas remotas
        for (tareaLocal in tareasLocal) {
            var encontrado = false
            for (tareaRemota in tareasRemotas) {
                if (tareaLocal.idGlobal == tareaRemota.idGlobal) {
                    // Si el ID es el mismo, comparar fechas (como cadenas)
                    if (tareaLocal.ultimaModificacion?.compareTo(tareaRemota.ultimaModificacion ?: "") == -1) {
                        tareasAActualizar.add(tareaRemota) // Actualizar con la remota
                    } else if (tareaRemota.ultimaModificacion?.compareTo(tareaLocal.ultimaModificacion ?: "") == -1) {
                        tareasAActualizar.add(tareaLocal) // Actualizar con la local
                    }
                    encontrado = true
                    break
                }
            }
            if (!encontrado) {
                // Si no se encuentra la tarea en la base de datos remota, la agregamos para insertar
                tareasAInsertar.add(tareaLocal)
            }
        }

        // Insertar tareas nuevas desde la remota (si no existen en la base de datos local)
        tareasRemotas.forEach { tareaRemota ->
            if (tareasLocal.none { it.idGlobal == tareaRemota.idGlobal }) {
                tareasAInsertar.add(tareaRemota) // Insertar si no existe en la local
            }
        }

        // Actualizar la base de datos local y también la remota
        tareasAActualizar.forEach { tarea ->
            if (tarea.id_tarea != null) {
                // Actualizar localmente
                db.actualizarProducto(tarea)

                // Llamada suspendida a la función de actualización remota
                try {
                    val respuesta = apiService.actualizarTarea(tarea)
                    if (respuesta.isSuccessful) {
                        // Éxito en la actualización remota
                    } else {
                        // Manejar error en la actualización remota
                    }
                } catch (e: Exception) {
                    // Manejar error en la actualización remota
                }
            }
        }

        // Insertar nuevas tareas en la base de datos local y remota
        tareasAInsertar.forEach { tarea ->
            // Verificar si la tarea ya existe en la base de datos local
            val tareaExistente = db.buscarProductoPorIdGlobal(tarea.idGlobal)
            if (tareaExistente == null) {
                // Si no existe, insertar la nueva tarea
                db.agregarProducto(tarea)
            } else {
                // Si la tarea ya existe, actualizarla
                db.actualizarProducto(tarea)
            }

            // Llamada suspendida a la función de inserción remota
            try {
                val respuesta = apiService.insertarTarea(tarea)
                if (respuesta.isSuccessful) {
                    // Éxito en la inserción remota
                } else {
                    // Manejar error en la inserción remota
                }
            } catch (e: Exception) {
                // Manejar error en la inserción remota
            }
        }

        // Eliminar tareas locales que ya no existen remotamente
        tareasAEliminar.forEach { tarea ->
            // Eliminar localmente
            db.eliminarProducto(tarea.id_tarea)

            // Llamada suspendida a la función de eliminación remota
            try {
                val tareaId = tarea.id_tarea ?: 0
                val respuesta = apiService.eliminarTarea(tareaId)
                if (respuesta.isSuccessful) {
                    // Éxito en la eliminación remota
                } else {
                    // Manejar error en la eliminación remota
                }
            } catch (e: Exception) {
                // Manejar error en la eliminación remota
            }
        }

        // Devuelve mensaje sobre el estado de la sincronización
        return "Sincronización completada: Actualizadas ${tareasAActualizar.size}, Insertadas ${tareasAInsertar.size}, Eliminadas ${tareasAEliminar.size}"
    }

    // Método para obtener todas las tareas locales activas
    fun obtenerTareasLocales(activo: Boolean = true): List<Tarea> {
        return db.obtenerProductos(activo) // Llamada al método de DBHelper para obtener tareas
    }

    // Método para eliminar una tarea específica por ID
    fun eliminarTarea(idTarea: Int?) {
        db.eliminarProducto(idTarea) // Llamada al método de DBHelper para eliminar tarea
    }

    // Método para buscar una tarea por ID
    fun buscarPorId(idTarea: Int): Tarea? {
        return db.buscarProducto(idTarea.toString()) // Llamada al método de DBHelper para buscar tarea por ID
    }

    // Método para guardar o actualizar una tarea
    suspend fun guardar(tarea: Tarea): String {
        return if (tarea.id_tarea == null || tarea.id_tarea == 0) {
            val idGlobalNuevo = java.util.UUID.randomUUID().toString()
            tarea.idGlobal = idGlobalNuevo

            // Guardar tarea en la base de datos local
            db.agregarProducto(tarea)

            // Llamada suspendida a la función de inserción remota
            try {
                val respuesta = apiService.insertarTarea(tarea)
                if (respuesta.isSuccessful) {
                    "Tarea agregada con éxito"
                } else {
                    "Error al agregar tarea remota"
                }
            } catch (e: Exception) {
                "Error en la inserción remota: ${e.message}"
            }
        } else {
            // Actualizar tarea en la base de datos local
            db.actualizarProducto(tarea)

            // Llamada suspendida a la función de actualización remota
            try {
                val respuesta = apiService.actualizarTarea(tarea)
                if (respuesta.isSuccessful) {
                    "Tarea modificada con éxito"
                } else {
                    "Error al modificar tarea remota"
                }
            } catch (e: Exception) {
                "Error en la actualización remota: ${e.message}"
            }
        }
    }
}


*/





