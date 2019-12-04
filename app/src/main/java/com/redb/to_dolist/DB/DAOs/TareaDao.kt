package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.redb.to_dolist.DB.Entidades.TareaEntity

@Dao
interface TareaDao {

    @Query("SELECT * FROM Tarea WHERE idTarea = :id")
    fun getTareaByID(id : String) : TareaEntity
}