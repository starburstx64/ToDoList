package com.redb.to_dolist.DB.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Usuario")
data class UsuarioEntity (
    @PrimaryKey @ColumnInfo(name = "idUsuario") val idUsuario:String,
    @ColumnInfo(name = "username") val username:String,
    @ColumnInfo(name = "password") val password:String,
    @ColumnInfo(name = "selectedAvatar") val selectedAvatar:Int,
    @ColumnInfo(name = "email") val email:String
)