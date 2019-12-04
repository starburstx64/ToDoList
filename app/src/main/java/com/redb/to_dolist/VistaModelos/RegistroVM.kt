package com.redb.to_dolist.VistaModelos

import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.redb.to_dolist.Modelos.Usuario

class RegistroVM : ViewModel() {
    lateinit var name: String
    lateinit var password: String
    lateinit var idUser: String
    lateinit var passwordConfirmacionRegistroActivity: String
    lateinit var correoElectronico: String
    var indexImage: Int = 0
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("App").child("users")
    var index = 0
    fun SetDatos(usuario: Usuario, passwordConfirmacion: String) {

        name = usuario.userName
        password = usuario.password
        passwordConfirmacionRegistroActivity = passwordConfirmacion
        correoElectronico = usuario.email
        indexImage = usuario.selectedAvatar
        idUser = ObtenerIdUsuario(correoElectronico)

    }

    fun ObtenerIdUsuario(email: String): String {
        var oldString = "@"
        var newString = ""
        var iduser = email.replace(oldString, newString, true)
        oldString = "."
        idUser = iduser.replace(oldString, newString, true)
        return idUser
    }

    fun RegistrarUsuario() {

        val user = Usuario(
            idUser.trim(),
            name.trim(),
            password.trim(),
            indexImage,
            correoElectronico.trim()
        )
        usersRef.push().setValue(user)
    }

}