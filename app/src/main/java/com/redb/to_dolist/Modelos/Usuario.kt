package com.redb.to_dolist.Modelos

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.redb.to_dolist.DB.AppDatabase

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

    fun TryIniciarSesion(db: AppDatabase, email: String, password: String): Int {
        val IniciarSesionUsuario = db.getUsuarioDao().getUsuario(email, password)
        if (IniciarSesionUsuario?.email == email) {
            if (IniciarSesionUsuario?.password == password) {
                // 0 inicio de sesion exitoso
                return 0
            } else {
                // ERROR: contraseña incorrecta
                return 1
            }

        } else return 2 //Correo Incorrecto
    }
}


