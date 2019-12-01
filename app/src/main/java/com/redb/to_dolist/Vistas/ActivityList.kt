package com.redb.to_dolist.Vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.redb.to_dolist.R
import kotlinx.android.synthetic.main.activity_list.*

class ActivityList : AppCompatActivity() {

    //region ControlesVista
    //EditText
    private lateinit var listName: EditText
    private lateinit var userName: EditText

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

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        //region InicializarControles
        listName = findViewById(R.id.list_edtiText_nameList)
        userName = findViewById(R.id.list_edtiText_userName)

        backgroundColor = findViewById(R.id.spinner_color)
        icon = findViewById(R.id.spinner_icono)

        checkShare = findViewById(R.id.list_checkBox_listaCompartida)

        addButton = findViewById(R.id.list_button_addUser)
        createButton = findViewById(R.id.list_button_crearLista)
        cancelButton = findViewById(R.id.list_button_cancelarLista)

        userRecycler = findViewById(R.id.list_recyclerView_showUser)
        //endregion
    }
}
