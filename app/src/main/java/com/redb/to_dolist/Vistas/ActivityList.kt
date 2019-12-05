package com.redb.to_dolist.Vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.R

class ActivityList : AppCompatActivity() {

    private var forEdit = false
    private lateinit var idList : String

    private lateinit var viewTitle : TextView
    private lateinit var listTitle : TextView
    private lateinit var listDescription : TextView
    private lateinit var colorSpinner : Spinner
    private lateinit var sharedCheckBox : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_list)

        viewTitle = findViewById(R.id.list_textView_addList)
        listTitle = findViewById(R.id.list_edtiText_nameList)
        listDescription = findViewById(R.id.list_inputText_description)
        colorSpinner = findViewById(R.id.list_spinner_color)
        sharedCheckBox = findViewById(R.id.list_checkBox_listaCompartida)

        forEdit = intent.getBooleanExtra("forEdit", false)

        if (forEdit) {
            idList = intent.getStringExtra("idList")!!

            val roomDatabase = AppDatabase.getAppDatabase(this)
            val currentList = roomDatabase.getListaDao().getListByID(idList)

            viewTitle.text = "Editar Lista"
            listTitle.setText(currentList.title, TextView.BufferType.EDITABLE)
            listDescription.setText(currentList.description, TextView.BufferType.EDITABLE)
            colorSpinner.setSelection(when (currentList.backgroundColor) {
                "Red" -> 0
                "Blue" -> 1
                "Yellow" -> 2
                "Green" -> 3
                else -> 0
            })

            sharedCheckBox.isChecked = currentList.shared
        }
    }

    fun getCurrentListID() : String {
        return idList
    }
}
