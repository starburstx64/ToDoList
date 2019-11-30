package com.redb.to_dolist.Vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.redb.to_dolist.R

class LoginActivity : AppCompatActivity() {
    private lateinit var Correo: EditText
    private lateinit var Contrasña: EditText
    private lateinit var IniciarSesion: Button
    private lateinit var Registrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Correo = findViewById(R.id.IniciarSesion)
        Contrasña = findViewById(R.id.Contraseña)
        IniciarSesion = findViewById(R.id.btnIniciar)
        Registrar = findViewById(R.id.btnRegistrartxt)

        Registrar.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }
    }

}
