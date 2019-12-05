package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.*
import com.redb.to_dolist.DB.Entidades.TareaEntity

@Dao
interface TareaDao {


    @Query("SELECT * FROM Tarea WHERE idTarea = :id")
    fun getTareaByID(id : String) : TareaEntity

    @Insert
    fun InsertarTarea(tarea:TareaEntity)

    @Query("SELECT idTarea FROM tarea WHERE idTarea=:idTarea")
    fun BuscarTarea(idTarea:String):List<String>

    @Delete
    fun BorrarTarea(tarea:TareaEntity)

    @Update
    fun ModificarTarea(tarea: TareaEntity)

    @Query("SELECT * FROM Tarea WHERE idLista=:idLista")
    fun getTaskFromList(idLista:String):List<TareaEntity>

    @Query("SELECT * FROM Tarea WHERE creator=:idUsuario")
    fun getAllTasks(idUsuario:String):List<TareaEntity>

    @Query("SELECT * FROM Tarea WHERE creator=:idUsuario AND dueDate!=NULL")
    fun getPlaneadasTasks(idUsuario:String):List<TareaEntity>

    @Query("SELECT * FROM Tarea WHERE creator=:idUsuario AND importance>0")
    fun getImportantTasks(idUsuario: String):List<TareaEntity>

}