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
    @PrimaryKey @ColumnInfo(name = "idTarea") val idTarea: Int,
    @ColumnInfo(name = "idLista") val idLista: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "importance") val importance: Int,
    @ColumnInfo(name = "dueDate") val dueDate: String,
    @ColumnInfo(name = "completed", typeAffinity = ColumnInfo.INTEGER) val completed: Boolean,
    @ColumnInfo(name = "creator") val creator: String,
    @ColumnInfo(name = "creatorName") val creatorName: String,
    @ColumnInfo(name = "creatorIcon") val creatorIcon: Int
)
