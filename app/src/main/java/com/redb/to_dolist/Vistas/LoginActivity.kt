package com.redb.to_dolist.Vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.google.android.material.snackbar.Snackbar
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.Modelos.Usuario
import com.redb.to_dolist.R

class LoginActivity : AppCompatActivity() {
    private lateinit var Correo: EditText
    private lateinit var Contrasña: EditText
    private lateinit var IniciarSesion: Button
    private lateinit var Registrar: Button
    private lateinit var UsuarioIniciarSesion: Usuario



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val db = AppDatabase.getAppDatabase(this)
        Correo = findViewById(R.id.IniciarSesion)
        Contrasña = findViewById(R.id.Contraseña)
        IniciarSesion = findViewById(R.id.btnIniciar)
        Registrar = findViewById(R.id.btnRegistrartxt)

        Registrar.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        IniciarSesion.setOnClickListener {

            var TryUsuarioIniciarSesion = UsuarioIniciarSesion.TryIniciarSesion(
                db, Correo.text.toString(),
                Contrasña.text.toString()
            )
            when (TryUsuarioIniciarSesion) {
                0 -> {
                    Snackbar.make(it, "Inicio de sesion exitoso!!", Snackbar.LENGTH_LONG).show()
                    val intent = Intent(this, ActivityList::class.java)
                    startActivity(intent)
                }
                1 -> Snackbar.make(it, "Contraseña Incorrecta", Snackbar.LENGTH_SHORT).show()

                2 -> Snackbar.make(it, "Correo Incorrecto", Snackbar.LENGTH_SHORT).show()
            }


        }
    }

}
