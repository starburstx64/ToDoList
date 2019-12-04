package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.redb.to_dolist.DB.Entidades.UsuarioEntity

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM Usuario WHERE idUsuario = :id")
    fun getUsuarioByID(id : String) : UsuarioEntity
}