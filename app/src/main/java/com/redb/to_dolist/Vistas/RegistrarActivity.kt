package com.redb.to_dolist.Vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.redb.to_dolist.R

class RegistrarActivity : AppCompatActivity() {
    private lateinit var nombreUsuario: EditText
    private lateinit var correoElectronico: EditText
    private lateinit var contraseña: EditText
    private lateinit var confirmarContraseña: EditText
    private lateinit var imagen: ImageView
    private lateinit var btnRegistrar: Button

    var fotoIndex: Int = 0
    val fotos = arrayOf(
        R.drawable.foto_01,
        R.drawable.foto_02,
        R.drawable.foto_03,
        R.drawable.foto_04,
        R.drawable.foto_05,
        R.drawable.foto_06,
        R.drawable.foto_07,
        R.drawable.foto_08,
        R.drawable.foto_09,
        R.drawable.foto_10,
        R.drawable.foto_11
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        nombreUsuario = findViewById(R.id.NombreUsuario)
        correoElectronico = findViewById(R.id.CorreoRegistro)
        contraseña = findViewById(R.id.ContraseñaRegistro)
        confirmarContraseña = findViewById(R.id.ConfirmarContraseñaRegistro)
        imagen = findViewById(R.id.ImagenId)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        imagen.setOnClickListener {
            seleccionarFoto()
            imagen.setImageResource(obtenerFoto(fotoIndex))
        }
    }

    fun seleccionarFoto() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elige una foto de perfil")

        val adaptadorDialogo =
            ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item)
        adaptadorDialogo.add("Mujer Zombi")
        adaptadorDialogo.add("Buffon")
        adaptadorDialogo.add("Payaso")
        adaptadorDialogo.add("Persona Loca")
        adaptadorDialogo.add("Fantasma")
        adaptadorDialogo.add("Rey")
        adaptadorDialogo.add("Hombre Calabaza")
        adaptadorDialogo.add("Reina")
        adaptadorDialogo.add("Esqueleto")
        adaptadorDialogo.add("Vikingo")
        adaptadorDialogo.add("Hombre Zombi")
        builder.setAdapter(adaptadorDialogo) { dialog, which ->
            fotoIndex = which

            imagen.setImageResource(obtenerFoto(fotoIndex))

        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()

        }
        builder.show()
    }

    fun obtenerFoto(indice: Int): Int {
        return fotos.get(indice)
    }
}
