package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Insert
import com.redb.to_dolist.DB.Entidades.InvitacionEntity

@Dao
interface InvitacionDao {

    @Insert
    fun insertInvitacion(newInvitation : InvitacionEntity) : Long
}