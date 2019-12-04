package com.redb.to_dolist.DB.DAOs

import androidx.room.*
import com.redb.to_dolist.DB.Entidades.TareaEntity

@Dao
interface TareaDao {

    @Insert
    fun InsertarTarea(tarea:TareaEntity)

    @Query("SELECT idTarea FROM tarea WHERE idTarea=:idTarea")
    fun BuscarTarea(idTarea:String):List<String>

    @Delete
    fun BorrarTarea(tarea:TareaEntity)

    @Update
    fun ModificarTarea(tarea: TareaEntity)
}