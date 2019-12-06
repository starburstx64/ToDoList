package com.redb.to_dolist.Vistas

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.stetho.Stetho
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.ListaEntity
import com.redb.to_dolist.DB.Entidades.TareaEntity
import com.redb.to_dolist.Modelos.FBModels.ListFB
import com.redb.to_dolist.Modelos.FBModels.Task
import com.redb.to_dolist.Modelos.FBModels.TaskInvitation
import com.redb.to_dolist.Modelos.FBModels.User
import com.redb.to_dolist.Modelos.Usuario
import com.redb.to_dolist.VistaModelos.MenuPrincipalVM
import com.redb.to_dolist.R

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
                R.id.nav_add_lists,
                R.id.nav_invitations
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerLayout = navView.getHeaderView(0)

        tvUserName=headerLayout.findViewById(R.id.nav_textView_userName)
        tvUserMail=headerLayout.findViewById(R.id.nav_textView_userMail)

        val menu = navView.menu
        val usuarioActual=db.getAplicacionDao().getLoggedUser()


        /*menu.findItem(R.id.nav_invitations).setOnMenuItemClickListener {
            navController.navigate(R.id.nav_invitations)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }*/
        menu.findItem(R.id.nav_button_all).setOnMenuItemClickListener {
            model.setCurrentTaskList(db.getTareaDao().getAllTasks(usuarioActual.toString()) as MutableList<TareaEntity>)
            navController.navigate(R.id.nav_home)
            drawerLayout.closeDrawer(GravityCompat.START)
            db.getAplicacionDao().setearLista("Todas")
            true
        }
        menu.findItem(R.id.nav_button_planing).setOnMenuItemClickListener {
            model.setCurrentTaskList(db.getTareaDao().getPlaneadasTasks(usuarioActual.toString()) as MutableList<TareaEntity>)
            navController.navigate(R.id.nav_home)
            drawerLayout.closeDrawer(GravityCompat.START)
            db.getAplicacionDao().setearLista("Planeadas")
            true
        }
        menu.findItem(R.id.nav_button_important).setOnMenuItemClickListener {
            model.setCurrentTaskList(db.getTareaDao().getImportantTasks(usuarioActual.toString()) as MutableList<TareaEntity>)
            navController.navigate(R.id.nav_home)
            drawerLayout.closeDrawer(GravityCompat.START)
            db.getAplicacionDao().setearLista("Importantes")
            true
        }
        menu.findItem(R.id.nav_button_closeSession).setOnMenuItemClickListener {
            val usuariologeado=db.getAplicacionDao().getLoggedUser()
            Usuario.DeslogearUsuario(usuariologeado.toString(),db)
            val intent = Intent(this@MenuPrincipalActivity, LoginActivity::class.java)
            startActivity(intent)
            true
        }

        db.getAplicacionDao().getAll()



//      val userReference1=database.getReference("App").child("users").setValue("holita")
        //val userReference=database.getReference("App").child("users").orderByChild("email").equalTo("raul@hotmail.com")
        val userReference=database.getReference("App").child("users").child(usuarioActual.toString())
        //database.getReference("App").child("prueba").setValue("holita")
        userReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var user:User? = p0.getValue(User::class.java)
                tvUserName.setText(user!!.username)
                tvUserMail.setText(user!!.email)            }
        })



        addListItems()
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
        val idUsuario=db.getAplicacionDao().getLoggedUser().toString()

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
                                    navController.navigate(R.id.nav_home)
                                    db.getAplicacionDao().setearLista(currentList.id.toString())
                                    drawerLayout.closeDrawer(GravityCompat.START)
                                    model.databaseRoom=db
                                    true
                                }.setIcon(GetIconoLista(currentList.listIcon))
                            }
                            else
                            {
                                val item =menu.add(1,Menu.NONE,2,currentList.title).setOnMenuItemClickListener {
                                    model.setSelectedList(currentList.id.toString())
                                    model.setCurrentTaskList(db.getTareaDao().getTaskFromList(currentList.id.toString()) as MutableList<TareaEntity>)
                                    navController.navigate(R.id.nav_home)
                                    db.getAplicacionDao().setearLista(currentList.id.toString())
                                    drawerLayout.closeDrawer(GravityCompat.START)
                                    true
                                }.setIcon(GetIconoLista(currentList.listIcon))
                            }
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
                                    CargarTareasListaActual()
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
                    })
                }
            }
        })
        CargarTareasListaActual()
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
                val usuarioActual = db.getUsuarioDao().getUsuarioByID(db.getAplicacionDao().getLoggedUser().toString())
                val listaActual = db.getListaDao().getListByID(db.getAplicacionDao().getAplicationList())
                if (usuarioActual.idUsuario==listaActual.idUsuario) {
                    val toEditListIntent = Intent(this, ActivityList::class.java)
                    toEditListIntent.putExtra("forEdit", true)
                    toEditListIntent.putExtra("idList", db.getAplicacionDao().getAplicationList())
                    startActivity(toEditListIntent)
                }
                else
                {
                    Toast.makeText(this,"No tienes permizo de editar esta lista",Toast.LENGTH_LONG).show()
                }

                true
            }

            R.id.action_bar_delete -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setMessage("¿Esta seguro que desea eliminar la lista actual")
                dialog.setTitle("Confirmar")
                dialog.setPositiveButton("Si") { _, _ ->


                    val usuarioActual = db.getUsuarioDao().getUsuarioByID(db.getAplicacionDao().getLoggedUser().toString())
                    val listaActual = db.getListaDao().getListByID(db.getAplicacionDao().getAplicationList())
                    if (usuarioActual.idUsuario==listaActual.idUsuario) {
                        val listInvitationsReference = database.getReference("App").child("listInvitations").child(listaActual.idLista)
                        listInvitationsReference.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(p0: DataSnapshot) {
                                p0.children.forEach{
                                    val actualList:TaskInvitation? = it.getValue(TaskInvitation::class.java)
                                    val userInvitacionReference =database.getReference("App").child("userInvitacions").child(actualList!!.idUser).child(it.key.toString()).setValue(null)

                                }
                            }

                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        })

                    }
                    else
                    {
                        Toast.makeText(this,"No tienes permizo de editar esta lista",Toast.LENGTH_LONG).show()
                    }
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

    fun CargarTareasListaActual()
    {
        var listaActual:String? = db.getAplicacionDao().getAplicationList()
        var usuarioActual= db.getAplicacionDao().getLoggedUser()
        if (listaActual!=null) {
            when (listaActual) {
                null -> model.setCurrentTaskList(db.getTareaDao().getAllTasks(usuarioActual.toString()).toMutableList())
                "Todas" -> model.setCurrentTaskList(db.getTareaDao().getAllTasks(usuarioActual.toString()).toMutableList())
                "Planeadas" -> model.setCurrentTaskList(
                    db.getTareaDao().getPlaneadasTasks(
                        usuarioActual.toString()
                    ).toMutableList()
                )
                "Importantes" -> model.setCurrentTaskList(
                    db.getTareaDao().getImportantTasks(
                        usuarioActual.toString()
                    ).toMutableList()
                )
                else -> model.setCurrentTaskList(db.getTareaDao().getTaskFromList(listaActual).toMutableList())
            }
        }
        //model.setCurrentTaskList(db.getTareaDao().getTaskFromList(listaActual).toMutableList())
    }

    fun handleInvitation(id : String, idList : String, accepted : Boolean) {
        val fbDatabase = FirebaseDatabase.getInstance()
        val roomDatabase = AppDatabase.getAppDatabase(this)
        val loggedUser = roomDatabase.getAplicacionDao().getLoggedUser()!!
        val userInvitationRef = fbDatabase.getReference("App").child("userInvitations").child(loggedUser).child(id)

        userInvitationRef.child("accepted").setValue(accepted)
        fbDatabase.getReference("App").child("listInvitations").child(idList).child(id).child("state").setValue(accepted)

        if (accepted) {
            fbDatabase.getReference("App").child("users").child(loggedUser).child("lists").child(idList).setValue(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finishAffinity()
    }

    fun GetIconoLista(numeroIcono:Int):Int
    {
        return when(numeroIcono)
        {
            1->R.drawable.trophy_gren
            2->R.drawable.icono_casita
            3->R.drawable.icono_nube
            else->R.drawable.trophy_gren
        }
    }
}
