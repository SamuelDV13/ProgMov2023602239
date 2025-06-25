package com.example.proyect2.datos


import com.example.proyect2.datos.ApiService.actualizarTarea
import com.example.proyect2.datos.ApiService.eliminarTareaRemota
import com.example.proyect2.datos.ApiService.obtenerTareasRemotas
import com.example.proyect2.datos.ApiService.subirTarea


suspend fun sincronizarTareas(dbHelper: TareaDBHelper) {
    val tareasLocales = dbHelper.obtenerTareas()
    val tareasRemotas = obtenerTareasRemotas()

    // 1. Eliminar en servidor las tareas locales marcadas como eliminadas
    for (tareaLocal in tareasLocales) {
        val remota = tareasRemotas.find { it.idGlobal == tareaLocal.idGlobal }
        if (tareaLocal.eliminado == 1 &&
            (remota == null || parseFecha(tareaLocal.ultimaModificacion ?: "") > parseFecha(remota.ultimaModificacion ?: ""))
        ) {
            eliminarTareaRemota(tareaLocal.idGlobal.toString())
        }
    }

    // 2. Eliminar localmente las tareas remotas marcadas como eliminadas
    for (tareaRemota in tareasRemotas) {
        val local = tareasLocales.find { it.idGlobal == tareaRemota.idGlobal }
        if (tareaRemota.eliminado == 1 &&
            (local == null || parseFecha(tareaRemota.ultimaModificacion ?: "") > parseFecha(local.ultimaModificacion ?: ""))
        ) {
            dbHelper.eliminarTareaPorIdGlobal(tareaRemota.idGlobal.toString())
        }
    }

    // 3. Subir tareas locales nuevas o más recientes
    for (tareaLocal in tareasLocales) {
        val remota = tareasRemotas.find { it.idGlobal == tareaLocal.idGlobal }
        val tareaPreparada = tareaLocal.copy(eliminado = tareaLocal.eliminado ?: 0)
        if (remota == null) {
            subirTarea(tareaPreparada)
        } else if (parseFecha(tareaLocal.ultimaModificacion ?: "") > parseFecha(remota.ultimaModificacion ?: "")) {
            actualizarTarea(tareaPreparada)
        }
    }

    // 4. Guardar tareas remotas nuevas o más recientes
    for (tareaRemota in tareasRemotas) {
        val local = tareasLocales.find { it.idGlobal == tareaRemota.idGlobal }
        if (local == null) {
            dbHelper.insertarTarea(tareaRemota)
        } else if (parseFecha(tareaRemota.ultimaModificacion ?: "") > parseFecha(local.ultimaModificacion ?: "")) {
            dbHelper.actualizarTarea(tareaRemota)
        }
    }
}




