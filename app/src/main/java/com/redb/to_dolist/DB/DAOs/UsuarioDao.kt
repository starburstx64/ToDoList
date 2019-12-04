package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.redb.to_dolist.DB.Entidades.UsuarioEntity
import com.redb.to_dolist.Modelos.Usuario

@Dao
interface UsuarioDao {
    @Query("INSERT INTO Usuario (idUsuario, username, password, selectedAvatar, email) VALUES (:idUsuario,:username, :password, :selectedAvatar, :email)")
    fun insertUsuario(idUsuario: String, username : String, password: String, selectedAvatar: Int,email:String)

    @Query("SELECT * FROM Usuario WHERE idUsuario = :idUsuario")
    fun getUsuarioByName(idUsuario : String) : UsuarioEntity?

    @Query("SELECT * FROM Usuario WHERE email = :email and password = :password")
    fun getUsuario(email: String , password: String): UsuarioEntity?

    @Query("SELECT * FROM Usuario WHERE idUsuario = :id")
    fun getUsuarioByID(id : String) : UsuarioEntity
}