package com.redb.to_dolist.DB.Entidades

import androidx.room.*


@Entity(
    tableName = "Invitacion",
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
data class InvitacionEntity(
    @PrimaryKey @ColumnInfo(name = "idInvitacion") val idInvitacion: String,
    @ColumnInfo(name = "idUsuario") val idUsuario: String,
    @ColumnInfo(name = "idLista") val idLista: String,
    @ColumnInfo(name = "listTitle") val listTitle:String,
    @ColumnInfo(name = "acepted", typeAffinity = ColumnInfo.INTEGER) val acepted: Boolean
)
