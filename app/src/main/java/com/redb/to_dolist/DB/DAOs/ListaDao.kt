package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.redb.to_dolist.DB.Entidades.ListaEntity

@Dao
interface ListaDao {

    @Insert
    fun insertList(newList : ListaEntity) : Long

    @Query("SELECT * FROM Lista WHERE idLista = :id")
    fun getListByID(id : String) : ListaEntity
  
    @Query("DELETE FROM Lista WHERE NOT (idLista IN (:idsExistentes))")
    fun DeleteUnexistenLists(idsExistentes:List<String>)

    @Query("SELECT idLista FROM lista WHERE idLista IN (:idsFirebase)")
    fun GetCurrentArchivedLists(idsFirebase:List<String>):List<String>

    @Insert
    fun InsertarLista(lista:ListaEntity)

}