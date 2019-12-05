package com.redb.to_dolist.DB.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.redb.to_dolist.DB.Entidades.InvitacionEntity

@Dao
interface InvitacionDao {

    @Insert
    fun insertInvitacion(newInvitation : InvitacionEntity) : Long

    @Query("SELECT * FROM Invitacion WHERE idUsuario = :id")
    fun getAllInvitationsByUserID(id : String) : List<InvitacionEntity>
}