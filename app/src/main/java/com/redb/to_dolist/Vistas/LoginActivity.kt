package com.redb.to_dolist.Vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.facebook.stetho.Stetho
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.UsuarioEntity
import com.redb.to_dolist.Modelos.FBModels.User
import com.redb.to_dolist.Modelos.Usuario
import com.redb.to_dolist.R

class LoginActivity : AppCompatActivity() {
    private lateinit var Correo: EditText
    private lateinit var Contrasña: EditText
    private lateinit var IniciarSesion: Button
    private lateinit var Registrar: Button

    private val database = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val db = AppDatabase.getAppDatabase(this)
        Correo = findViewById(R.id.IniciarSesion)
        Contrasña = findViewById(R.id.Contraseña)
        IniciarSesion = findViewById(R.id.btnIniciar)
        Registrar = findViewById(R.id.btnRegistrartxt)
        Stetho.initializeWithDefaults(this)

        Registrar.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        IniciarSesion.setOnClickListener {

            val loginReference = database.getReference("App").child("users").orderByChild("email")
                .equalTo(Correo.text.toString())
            loginReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (!p0.exists()) {
                        Snackbar.make(it, "Correo no registrado", Snackbar.LENGTH_SHORT).show()
                    }
                    var user=User()
                    user.username=    p0.getValue(User::class.java)!!.username
                    user.email=    p0.getValue(User::class.java)!!.email
                    user.password=    p0.getValue(User::class.java)!!.password
                    user.selectedAvatar=    p0.getValue(User::class.java)!!.selectedAvatar
                    user.confirmed=    p0.getValue(User::class.java)!!.confirmed


                    var hijoEstoEsSal= p0.children.forEach{
                       user.username=it.child("username").value.toString()
                        user.password=it.child("password").value.toString()
                        user.id = it.key
                        user.email=it.child("email").value.toString()
                        user.selectedAvatar=it.child("selectedAvatar").value.toString().toInt()
                        user.confirmed=it.child("confirmed").value.toString().toBoolean()
                    }





                    if (user.confirmed == false)
                        Snackbar.make(it, "Usuario aun no aprobado", Snackbar.LENGTH_SHORT).show()
                    else if (user.password != Contrasña.text.toString())
                        Snackbar.make(it, "Contraseña incorrecta", Snackbar.LENGTH_SHORT).show()
                    else { //Si si nos logeamos exitosamente

                        val logedUserKey = user.id.toString()
                        var UserIniciarSesion = UsuarioEntity(
                            user.id!!,
                            user.username,
                            user.password,
                            user.selectedAvatar,
                            user.email

                        )

                        Usuario.LogearUsuario(UserIniciarSesion, db)
                        val intent = Intent(this@LoginActivity, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                    }

                }
            })


        }
    }

}
