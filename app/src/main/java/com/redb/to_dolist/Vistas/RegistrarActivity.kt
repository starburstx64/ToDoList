package com.redb.to_dolist.Vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.facebook.stetho.Stetho
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.redb.to_dolist.R
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.Modelos.Usuario
import com.redb.to_dolist.VistaModelos.RegistroVM

class RegistrarActivity : AppCompatActivity() {
    private lateinit var nombreUsuario: EditText
    private lateinit var correoElectronico: EditText
    private lateinit var contraseña: EditText
    private lateinit var confirmarContraseña: EditText
    private lateinit var imagen: ImageView
    private lateinit var btnRegistrar: Button

    private lateinit var btnprueba: Button
    private val model by lazy { ViewModelProviders.of(this)[RegistroVM::class.java] }
    private var index = 0

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

        btnRegistrar = findViewById(R.id.btnRegistrar)
        imagen = findViewById(R.id.ImagenId)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        imagen.setOnClickListener {
            seleccionarFoto()
            imagen.setImageResource(obtenerFoto(fotoIndex))
        }
        btnRegistrar.setOnClickListener {

            if (nombreUsuario.text.isEmpty() ||
                correoElectronico.text.isEmpty() ||
                contraseña.text.isEmpty() ||
                confirmarContraseña.text.isEmpty()
            ) {
                Snackbar.make(it, "Llene todos los campos", Snackbar.LENGTH_LONG).show()
            } else {
                if (contraseña.text.toString() == confirmarContraseña.text.toString()) {
                    var usuarioRegistro = Usuario(
                        correoElectronico.text.toString(),
                        nombreUsuario.text.toString(),
                        contraseña.text.toString(),
                        fotoIndex,
                        correoElectronico.text.toString()
                    )
                    model.SetDatos(usuarioRegistro, confirmarContraseña.text.toString())
                    val database = FirebaseDatabase.getInstance()
                    val usersRef = database.getReference("App").child("users")

                    val loginReference =
                        usersRef.orderByChild("email").equalTo(correoElectronico.text.toString())
                    loginReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            if (!dataSnapshot.hasChildren() && !dataSnapshot.children.iterator().hasNext()) {
                                confirmarContraseña.text.clear()
                                contraseña.text.clear()
                                nombreUsuario.text.clear()
                                correoElectronico.text.clear()
                                model.RegistrarUsuario()
                                Snackbar.make(it, "Registro Exitoso", Snackbar.LENGTH_LONG).show()

                                val intent =
                                    Intent(this@RegistrarActivity, LoginActivity::class.java)
                                startActivity(intent)
                            } else Snackbar.make(
                                it,
                                "El Correo Proporcionado ya existe",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            throw databaseError.toException() as Throwable
                        }
                    })
                } else Snackbar.make(
                    it,
                    "Las contraseñas no coinciden",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    fun seleccionarFoto() {
        imagen.resources
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elige una foto de perfil")

        val adaptadorDialogo =
            ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item)
        adaptadorDialogo.add("Mujer Zombie")
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

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
