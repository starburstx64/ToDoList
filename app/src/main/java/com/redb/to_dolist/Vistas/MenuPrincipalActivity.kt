package com.redb.to_dolist.Vistas

import android.content.ClipData
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.TextView
import com.facebook.stetho.Stetho
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.Modelos.FBModels.User
import com.redb.to_dolist.R


class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var tvUserName:TextView
    private lateinit var tvUserMail:TextView
    private lateinit var btnSincronizar:ClipData.Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar_toolbar)
        setSupportActionBar(toolbar)

        Stetho.initializeWithDefaults(this)
        val db =AppDatabase.getAppDatabase(this)

        val fab: FloatingActionButton = findViewById(R.id.main_fab_addButton)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerLayout = navView.getHeaderView(0)

        tvUserName=headerLayout.findViewById(R.id.nav_textView_userName)
        tvUserMail=headerLayout.findViewById(R.id.nav_textView_userMail)

        val menu = navView.menu

        menu.findItem(R.id.nav_button_sync).setOnMenuItemClickListener {
            tvUserName.setText("probando sincronizacion")
            true
        }

        db.getAplicacionDao().getAll()
        val database=FirebaseDatabase.getInstance()

//      val userReference1=database.getReference("App").child("users").setValue("holita")
//        val userReference=database.getReference("App").child("users").orderByChild("email").equalTo("david@hotmail.com")
        val userReference=database.getReference("App").child("users").orderByKey().equalTo("raulhotmailcom")
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



        addMenuItemInNavMenuDrawer()


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

    private fun addMenuItemInNavMenuDrawer() {
        val navView = findViewById(R.id.nav_view) as NavigationView

        val menu = navView.menu

        val item = menu.add(0,Menu.NONE,1,"Listas").setIcon(R.drawable.ic_feedback_black_24dp).setOnMenuItemClickListener {
            tvUserName.setText("hola")
            true
        }
        //item.setActionView(R.layout.fragment_home)

        menu.add(0,Menu.NONE,1,"Listas2")

        navView.invalidate()
    }
}
