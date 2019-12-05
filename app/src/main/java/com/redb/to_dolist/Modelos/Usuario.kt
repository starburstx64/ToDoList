package com.redb.to_dolist.Modelos

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.UsuarioEntity

data class Usuario(
    val idUsuario: String,
    var userName: String,
    var password: String,
    var selectedAvatar: Int,
    val email: String
) {

    fun TryRegistrarUsuario(db: AppDatabase, usuario: Usuario):Int {
        val usuarioExiste = db.getUsuarioDao().getUsuarioByName(usuario.idUsuario)

        if (usuarioExiste?.email != usuario.email) {

            db.getUsuarioDao().insertUsuario(
                usuario.idUsuario,
                usuario.userName,
                usuario.password,
                usuario.selectedAvatar,
                usuario.email
            )
            //0 se registro con exito
            return 0
        } else {
            //1 ERROR:el correo ya existe
            return 1

        }


    }

    companion object{
        fun LogearUsuario(user:UsuarioEntity,db: AppDatabase){
            db.getUsuarioDao().deslogearUsuario(user.idUsuario)
            db.getUsuarioDao().InsertarUsuario(user)
            db.getAplicacionDao().logearUsuario(user.idUsuario)

        }

        fun DeslogearUsuario(idUsuario: String,db:AppDatabase)
        {
            db.getUsuarioDao().deslogearUsuario(idUsuario)
        }

    }




}


