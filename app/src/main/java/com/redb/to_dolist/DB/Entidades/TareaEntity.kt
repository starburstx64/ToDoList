package com.redb.to_dolist.DB.Entidades

import androidx.room.*
import java.util.*

@Entity(
    tableName = "Tarea",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ListaEntity::class,
            parentColumns = arrayOf("idLista"),
            childColumns = arrayOf("idLista"),
            onDelete = ForeignKey.CASCADE
        )
    ),
    indices = [Index("idLista")]
)
data class TareaEntity(
    @PrimaryKey @ColumnInfo(name = "idTarea") val idTarea: String,
    @ColumnInfo(name = "idLista") val idLista: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "importance") var importance: Int=0,
    @ColumnInfo(name = "dueDate") var dueDate: Int?=null,
    @ColumnInfo(name = "completed", typeAffinity = ColumnInfo.INTEGER) var completed: Boolean,
    @ColumnInfo(name = "creator") val creator: String,
    @ColumnInfo(name = "creatorName") var creatorName: String,
    @ColumnInfo(name = "creatorIcon") var creatorIcon: Int,
    @ColumnInfo(name = "description") var descrition: String
)
