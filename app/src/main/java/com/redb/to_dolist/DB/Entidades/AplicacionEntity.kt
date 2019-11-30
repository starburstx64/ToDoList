package com.redb.to_dolist.DB.Entidades

import androidx.room.*

@Entity(tableName = "Aplicacion",
    foreignKeys = arrayOf(ForeignKey(entity = UsuarioEntity::class,
        parentColumns = arrayOf("idUsuario"),
        childColumns = arrayOf("logedUser"),
        onDelete = ForeignKey.CASCADE)),
    indices = [Index("logedUser")]
)

data class AplicacionEntity (
    @PrimaryKey @ColumnInfo(name = "idAplicacion") val idAplicacion:Int,
    @ColumnInfo(name = "logedUser") val logedUser:String?,
    @ColumnInfo(name = "lastView") val lastView:String
)