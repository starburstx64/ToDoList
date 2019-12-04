package com.redb.to_dolist.Vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.redb.to_dolist.Modelos.Usuario
import com.redb.to_dolist.R
import android.widget.Toast
import android.app.AlertDialog
import com.redb.to_dolist.DB.AppDatabase


class ActivityList : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    class Adapter(private val activity : ActivityList) : RecyclerView.Adapter<Adapter.AdapterViewHolder>() {

        class AdapterViewHolder(private val view : View, private val activity: ActivityList) : RecyclerView.ViewHolder(view) {
            private var textViewUserName : TextView = view.findViewById(R.id.list_textView_rvUserName)

            fun bind(user : Usuario) {
                textViewUserName.text = user.userName

                view.findViewById<ImageView>(R.id.list_imageView_rvDeleteUserButton).setOnClickListener {
                    activity.deleteUserAtPostion(layoutPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_user_list, parent, false) as View

            return AdapterViewHolder(view, activity)
        }

        override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
            holder.bind(activity.userList[position])
        }

        override fun getItemCount(): Int {
            return activity.userList.size
        }
    }

    //region ControlesVista
    //EditText
    private lateinit var listNameEditText: EditText
    private lateinit var userNameEditText: EditText

    //Spinners
    private lateinit var backgroundColor: Spinner
    private lateinit var icon: Spinner

    //CheckBox
    private lateinit var checkShare: CheckBox

    //Buttons
    private lateinit var addButton: Button
    private lateinit var createButton: Button
    private lateinit var cancelButton: Button

    //RecyclerView
    private lateinit var userRecycler: RecyclerView

    private var database = FirebaseDatabase.getInstance()

    private var userList = arrayListOf<Usuario>()

    private val colors = listOf("Red", "Blue", "Yellow", "Green")
    private var selectedColor = 0

    //endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
      
        //region InicializarControles
        listNameEditText = findViewById(R.id.list_edtiText_nameList)
        userNameEditText = findViewById(R.id.list_editText_userName)


        backgroundColor = findViewById(R.id.list_spinner_color)
        icon = findViewById(R.id.list_spinner_icono)

        checkShare = findViewById(R.id.list_checkBox_listaCompartida)

        addButton = findViewById(R.id.list_button_addUser)
        createButton = findViewById(R.id.list_button_crearLista)
        cancelButton = findViewById(R.id.list_button_cancelarLista)

        val spintAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors)
        spintAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        backgroundColor.adapter = spintAdapter
        backgroundColor.onItemSelectedListener = this

        userRecycler = findViewById<RecyclerView>(R.id.list_recyclerView_showUser).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ActivityList)
            adapter = Adapter(this@ActivityList)
        }

        //endregion
        addButton.setOnClickListener {
            database.getReference("App").child("users").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        val text = userNameEditText.text.toString().filterNot { c -> "@.".contains(c) }

                        if (text.isNotEmpty() && p0.child(text).exists()) {
                            val userName =  p0.child(text).child("username").value.toString()

                            userList.add(Usuario("sada", userName, "asda", 1, text))
                            userRecycler.adapter?.notifyDataSetChanged()

                            userNameEditText.text.clear()
                        }

                        else {
                            val dialog = AlertDialog.Builder(this@ActivityList)
                            dialog.setMessage("No se encontro un usuario para el correo seleccionado")
                            dialog.setTitle("Correo no encontrado")
                            dialog.setPositiveButton("Ok") { _, _ ->

                            }

                            val alertDialog = dialog.create()
                            alertDialog.show()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
            })
        }

        createButton.setOnClickListener {
            val loggedUser = AppDatabase.getAppDatabase(this).getAplicacionDao().getLoggedUser()

            val listsRef = database.getReference("App").child("lists").push()
            listsRef.child("creator").setValue(loggedUser.toString())
            listsRef.child("shared").setValue(checkShare.isChecked)
            listsRef.child("backgroundColor").setValue(colors[selectedColor])
            listsRef.child("title").setValue(listNameEditText.text.toString())
            listsRef.child("description").setValue("Prueba en 3, 2, 1...")

            val userInvationsRef = database.getReference("App").child("userInvitations")
            userList.forEach {
                listsRef.child("users").child(it.email).setValue(true)

                val invitationRef = userInvationsRef.child(it.email).child("one")
                invitationRef.child("idList").setValue(listsRef.key)
                invitationRef.child("listTitle").setValue(listNameEditText.text.toString())
            }

            Toast.makeText(this, "Lista Creada Correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    fun getUserList() : ArrayList<Usuario> {
        return userList
    }

    fun deleteUserAtPostion(position : Int) {
        userList.removeAt(position)
        userRecycler.adapter?.notifyDataSetChanged()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedColor = p2
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}
