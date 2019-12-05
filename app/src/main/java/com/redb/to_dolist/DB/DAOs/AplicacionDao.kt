package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.redb.to_dolist.DB.Entidades.AplicacionEntity

@Dao
interface AplicacionDao {

    @Query("SELECT * FROM Aplicacion")
    fun getAll():List<AplicacionEntity>

    @Query("SELECT logedUser FROM Aplicacion WHERE idAplicacion = 0")
    fun getLoggedUser() : String?

    @Query("UPDATE Aplicacion SET logedUser=:id")
    fun logearUsuario(id:String)

    @Query("UPDATE Aplicacion SET lastView=:listId")
    fun setearLista(listId:String)

    @Query("SELECT lastView FROM Aplicacion WHERE idAplicacion=0")
    fun getAplicationList():String
}