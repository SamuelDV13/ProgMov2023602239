package com.example.proyect2.datos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class TareaDBHelper(context: Context) : SQLiteOpenHelper(context, "agenda.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE tareas (
                id_tarea INTEGER PRIMARY KEY AUTOINCREMENT,
                idGlobal TEXT,
                titulo TEXT NOT NULL,
                fecha TEXT,
                prioridad TEXT NOT NULL,
                estado TEXT NOT NULL,
                descripcion TEXT,
                notificacion INTEGER,
                ultimaModificacion TEXT,
                eliminado INTEGER NOT NULL DEFAULT 0
            );
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tareas")
        onCreate(db)
    }

    fun insertarTarea(tarea: Tarea): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("idGlobal", tarea.idGlobal)
            put("titulo", tarea.titulo)
            put("fecha", tarea.fecha)
            put("prioridad", tarea.prioridad)
            put("estado", tarea.estado)
            put("descripcion", tarea.descripcion)
            put("notificacion", tarea.notificacion)
            put("ultimaModificacion", tarea.ultimaModificacion)
            put("eliminado", tarea.eliminado)
        }
        return db.insert("tareas", null, values)
    }

    fun obtenerTareas(): List<Tarea> {
        val db = readableDatabase
        val cursor = db.query("tareas", null, "eliminado = 0", null, null, null, "fecha ASC")
        return cursorToList(cursor)
    }

    fun obtenerTareaPorId(id: Int): Tarea? {
        val db = readableDatabase
        val cursor = db.query("tareas", null, "id_tarea = ?", arrayOf(id.toString()), null, null, null)
        return if (cursor.moveToFirst()) cursorToTarea(cursor) else null
    }

    fun actualizarTarea(tarea: Tarea): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("idGlobal", tarea.idGlobal)
            put("titulo", tarea.titulo)
            put("fecha", tarea.fecha)
            put("prioridad", tarea.prioridad)
            put("estado", tarea.estado)
            put("descripcion", tarea.descripcion)
            put("notificacion", tarea.notificacion)
            put("ultimaModificacion", obtenerFechaActual())
            put("eliminado", tarea.eliminado)
        }
        return db.update("tareas", values, "id_tarea = ?", arrayOf(tarea.id_tarea.toString()))
    }

    // Elimina por id_tarea (uso local, botón eliminar)
    fun eliminarTareaPorIdLocal(id: Int?): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("eliminado", 1)
            put("ultimaModificacion", obtenerFechaActual())
        }
        return db.update("tareas", values, "id_tarea = ?", arrayOf(id.toString()))
    }

    // Elimina por idGlobal (uso en sincronización)
    fun eliminarTareaPorIdGlobal(idGlobal: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("eliminado", 1)
            put("ultimaModificacion", obtenerFechaActual())
        }
        return db.update("tareas", values, "idGlobal = ?", arrayOf(idGlobal))
    }



    private fun cursorToList(cursor: Cursor): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        while (cursor.moveToNext()) {
            tareas.add(cursorToTarea(cursor))
        }
        cursor.close()
        return tareas
    }

    private fun cursorToTarea(cursor: Cursor): Tarea {
        return Tarea(
            id_tarea = cursor.getInt(cursor.getColumnIndexOrThrow("id_tarea")),
            idGlobal = cursor.getString(cursor.getColumnIndexOrThrow("idGlobal")),
            titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
            prioridad = cursor.getString(cursor.getColumnIndexOrThrow("prioridad")),
            estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")),
            descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
            notificacion = cursor.getInt(cursor.getColumnIndexOrThrow("notificacion")),
            ultimaModificacion = cursor.getString(cursor.getColumnIndexOrThrow("ultimaModificacion")),
            eliminado = cursor.getInt(cursor.getColumnIndexOrThrow("eliminado"))
        )
    }

    fun obtenerTareasOrdenadasPorFecha(): List<Tarea> {
        val db = readableDatabase
        val cursor = db.query("tareas", null, "eliminado = 0", null, null, null, "fecha ASC")
        return cursorToList(cursor)
    }

    fun obtenerTareasOrdenadasPorPrioridad(): List<Tarea> {
        val db = readableDatabase
        val cursor = db.query("tareas", null, "eliminado = 0", null, null, null, "prioridad ASC")
        return cursorToList(cursor)
    }

}
