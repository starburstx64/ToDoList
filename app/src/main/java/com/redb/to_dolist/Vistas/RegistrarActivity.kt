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

    private lateinit var btnprueba:Button
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

        val db = AppDatabase.getAppDatabase(this)
        Stetho.initializeWithDefaults(this)

        nombreUsuario = findViewById(R.id.NombreUsuario)
        correoElectronico = findViewById(R.id.CorreoRegistro)
        contraseña = findViewById(R.id.ContraseñaRegistro)
        confirmarContraseña = findViewById(R.id.ConfirmarContraseñaRegistro)

        btnRegistrar = findViewById(R.id.btnRegistrar)
        imagen = findViewById(R.id.ImagenId)





        /*  usersRef.addChildEventListener(object : ChildEventListener {
              override fun onCancelled(p0: DatabaseError) {

              }

              override fun onChildMoved(p0: DataSnapshot, p1: String?) {

              }

              override fun onChildChanged(p0: DataSnapshot, p1: String?) {

              }

              override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                  val user: Usuario? = p0.getValue(Usuario::class.java)
                  user?.userName = p0.key!!
              }

              override fun onChildRemoved(p0: DataSnapshot) {

              }


          })*/

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


    fun obtnerIndice(num: Int) {
        val indice = num
        index = indice

    }
}
