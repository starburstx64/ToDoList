package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Insert
import com.redb.to_dolist.DB.Entidades.ListaEntity

@Dao
interface ListaDao {

    @Insert
    fun insertList(newList : ListaEntity) : Long
}