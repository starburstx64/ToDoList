package com.redb.to_dolist.Vistas

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.stetho.Stetho
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.*
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.ListaEntity
import com.redb.to_dolist.DB.Entidades.TareaEntity
import com.redb.to_dolist.Modelos.FBModels.ListFB
import com.redb.to_dolist.Modelos.FBModels.Task
import com.redb.to_dolist.Modelos.FBModels.User
import com.redb.to_dolist.Modelos.Lista
import com.redb.to_dolist.R
import com.redb.to_dolist.VistaModelos.MenuPrincipalVM

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var tvUserName:TextView
    private lateinit var tvUserMail:TextView
    private lateinit var btnSincronizar:ClipData.Item

    private val model by lazy { ViewModelProviders.of(this).get(MenuPrincipalVM::class.java) }
    private lateinit var db:AppDatabase
    private val database=FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar_toolbar)
        setSupportActionBar(toolbar)

        Stetho.initializeWithDefaults(this)
        db =AppDatabase.getAppDatabase(this)

//        val fab: FloatingActionButton = findViewById(R.id.main_fab_addButton)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_add_lists
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerLayout = navView.getHeaderView(0)

        tvUserName=headerLayout.findViewById(R.id.nav_textView_userName)
        tvUserMail=headerLayout.findViewById(R.id.nav_textView_userMail)

        val menu = navView.menu

        menu.findItem(R.id.nav_button_sync).setOnMenuItemClickListener {
            //tvUserName.setText("probando sincronizacion")

            true
        }

        db.getAplicacionDao().getAll()



//      val userReference1=database.getReference("App").child("users").setValue("holita")
        //val userReference=database.getReference("App").child("users").orderByChild("email").equalTo("raul@hotmail.com")
        val userReference=database.getReference("App").child("users").orderByKey().equalTo("raulhotmailcom")
        //database.getReference("App").child("prueba").setValue("holita")
        userReference.addChildEventListener(object : ChildEventListener{
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val user:User? = p0.getValue(User::class.java)
                tvUserName.setText(user!!.username)
                tvUserMail.setText(user!!.email)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user:User? = p0.getValue(User::class.java)
                user?.id =p0.key

                tvUserName.setText(user!!.username)
                tvUserMail.setText(user!!.email)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })



        //addListItems()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun addListItems() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_add_lists
            ), drawerLayout
        )
        //Obteniendo el menu
        val menu = navView.menu

        //obtener la llave de nuestro usuario
        val idUsuario="raulhotmailcom"

        //PRUEBA DE ELIMINADO DE LISTA
        //database.getReference("App").child("users").child(idUsuario).child("lists").child("-LvDbu4lMwtaYhMhkTtG").setValue(null)
        //OBTENER AL OBJETO USUARIO PARA OBTENER LAS LISTAS QUE TIENE
        val userReference=database.getReference("App").child("users").child(idUsuario)
        userReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentUser:User? = p0.getValue(User::class.java)

                var idsListas = currentUser!!.lists.keys

                db.getListaDao().DeleteUnexistenLists(idsListas.toList())

                var listasActualmenteSincronizadas = db.getListaDao().GetCurrentArchivedLists(idsListas.toList())

                menu.removeGroup(0)
                menu.removeGroup(1)
                //menu.removeGroup(R.id.group_listas)

                for (idLista in idsListas)
                {
                    val listReference=database.getReference("App").child("lists").child(idLista)
                    listReference.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            val currentList:ListFB? = p0.getValue(ListFB::class.java)
                            currentList!!.id=p0.key

                            if(currentList.id !in listasActualmenteSincronizadas)
                            {
                                val ListaAInsertar = ListaEntity(
                                    currentList.id.toString(),
                                    currentList.creator,
                                    currentList.title,
                                    currentList.description,
                                    currentList.creator,"",
                                    currentList.shared,
                                    currentList.listIcon,
                                    currentList.backgroundColor)
                                db.getListaDao().InsertarLista(ListaAInsertar)
                            }

                            if(currentList!!.shared==false)
                            {
                                val item = menu.add(0,Menu.NONE,1,currentList.title).setOnMenuItemClickListener {
                                    model.setSelectedList(currentList.id.toString())
                                    model.setCurrentTaskList(db.getTareaDao().getTaskFromList(currentList.id.toString()) as MutableList<TareaEntity>)
                                    true
                                }
                            }
                            else
                            {
                                val item =menu.add(1,Menu.NONE,2,currentList.title).setOnMenuItemClickListener {
                                    model.setSelectedList(currentList.id.toString())
                                    model.setCurrentTaskList(db.getTareaDao().getTaskFromList(currentList.id.toString()) as MutableList<TareaEntity>)
                                    true
                                }
                            }
                        }
                    })
                }

                for (idLista in idsListas)
                {
                    val tasksReference= database.getReference("App").child("tasks").child(idLista)
                    tasksReference.addChildEventListener(object : ChildEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                            val currentTask:Task?=p0.getValue(Task::class.java)
                            currentTask!!.id=p0.key

                            var TareaModificar:TareaEntity=TareaEntity(
                                currentTask.id.toString(),
                                idLista,
                                currentTask.title,
                                currentTask.importance,
                                currentTask.duedate,
                                currentTask.completed,
                                currentTask.creator,
                                currentTask.creatorName,
                                currentTask.creatorIcon,
                                currentTask.description
                            )
                            db.getTareaDao().ModificarTarea(TareaModificar)
                        }

                        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                            val currentTask:Task?=p0.getValue(Task::class.java)
                            currentTask!!.id=p0.key

                            if(db.getTareaDao().BuscarTarea(currentTask.id.toString()).isEmpty())
                            {
                                var TareaInsertar:TareaEntity=TareaEntity(
                                    currentTask.id.toString(),
                                    idLista,
                                    currentTask.title,
                                    currentTask.importance,
                                    currentTask.duedate,
                                    currentTask.completed,
                                    currentTask.creator,
                                    currentTask.creatorName,
                                    currentTask.creatorIcon,
                                    currentTask.description
                                )

                                db.getTareaDao().InsertarTarea(TareaInsertar)
                            }
                        }

                        override fun onChildRemoved(p0: DataSnapshot) {
                            val currentTask:Task?=p0.getValue(Task::class.java)
                            currentTask!!.id=p0.key

                            var TareaBorrar:TareaEntity=TareaEntity(
                                currentTask.id.toString(),
                                idLista,
                                currentTask.title,
                                currentTask.importance,
                                currentTask.duedate,
                                currentTask.completed,
                                currentTask.creator,
                                currentTask.creatorName,
                                currentTask.creatorIcon,
                                currentTask.description
                            )

                            db.getTareaDao().BorrarTarea(TareaBorrar)
                        }
                    })
                }
            }
        })


//        val item = menu.add(0,Menu.NONE,1,"Listas").setIcon(R.drawable.ic_feedback_black_24dp).setOnMenuItemClickListener {
//            tvUserName.setText("hola")
//            navController.navigate(R.id.nav_home)
//            true
//        }
//        //item.setActionView(R.layout.fragment_home)
//
//        menu.add(0,Menu.NONE,1,"Listas2")

        navView.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_bar_sort -> {

                val dialog = Dialog(this)
                dialog.setContentView(R.layout.task_sort_dialog)
                dialog.setCancelable(true)

                val btnSort = dialog.findViewById<Button>(R.id.task_button_sort)
                val btnCancel = dialog.findViewById<Button>(R.id.task_button_cancel)
                val radDate = dialog.findViewById<RadioButton>(R.id.task_radioButton_date)
                val radAsc = dialog.findViewById<RadioButton>(R.id.task_radioButton_asc)

                btnCancel.setOnClickListener {
                    dialog.cancel()
                }

                btnSort.setOnClickListener {
                    model.sortList(radDate.isChecked, radAsc.isChecked)
                    dialog.hide()
                }

                dialog.show()

                true
            }

            R.id.actionbar_edit -> {
                // Abrir editar lista
                true
            }

            R.id.action_bar_delete -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setMessage("¿Esta seguro que desea eliminar la lista actual")
                dialog.setTitle("Confirmar")
                dialog.setPositiveButton("Si") { _, _ ->

                }

                dialog.setNegativeButton("No") {_, _->

                }

                val alertDialog = dialog.create()
                alertDialog.show()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
