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
}