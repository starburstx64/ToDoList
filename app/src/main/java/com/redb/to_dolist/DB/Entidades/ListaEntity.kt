package com.redb.to_dolist.DB.Entidades

import androidx.room.*

@Entity(
    tableName = "Lista",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = arrayOf("idUsuario"),
            childColumns = arrayOf("idUsuario"),
            onDelete = ForeignKey.CASCADE
        )
    ),
    indices = [Index("idUsuario")]
)
data class ListaEntity(
    @PrimaryKey @ColumnInfo(name = "idLista") val idLista: String,
    @ColumnInfo(name = "idUsuario") val idUsuario: String,
    @ColumnInfo(name = "creator") val creator: String,
    @ColumnInfo(name = "creatorName") val creatorName: String,
    @ColumnInfo(name = "shared", typeAffinity = ColumnInfo.INTEGER) val shared: Boolean,
    @ColumnInfo(name = "listIcon") val ListIcon: Int
)
