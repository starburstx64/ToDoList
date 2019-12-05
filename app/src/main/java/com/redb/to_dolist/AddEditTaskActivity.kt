package com.redb.to_dolist

import android.os.Bundle
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.redb.to_dolist.DB.AppDatabase

class AddEditTaskActivity : FragmentActivity() {

    lateinit var addEdit_TextView_Header: TextView
    lateinit var addEdit_EditText_Nombre: EditText
    lateinit var addEdit_EditText_Descripcion: EditText
    lateinit var addEdit_RadioButton_SinAsignar: RadioButton
    lateinit var addEdit_RadioButton_Alto: RadioButton
    lateinit var addEdit_RadioButton_Medio: RadioButton
    lateinit var addEdit_RadioButton_Bajo: RadioButton
    lateinit var addEdit_DatePicker_fecha: DatePicker
    lateinit var addEdit_CheckBox_AceptarFecha: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        addEdit_TextView_Header = findViewById(R.id.addEdit_textView_header)
        addEdit_EditText_Descripcion = findViewById(R.id.addEdit_editText_descripcion)
        addEdit_EditText_Nombre = findViewById(R.id.addEdit_editText_nombre)

        //RadioButtons de nivel de importancia
        addEdit_RadioButton_SinAsignar = findViewById(R.id.addEdit_radioButton_sinAsignar)
        addEdit_RadioButton_Alto = findViewById(R.id.addEdit_radioButton_alta)
        addEdit_RadioButton_Medio = findViewById(R.id.addEdit_radioButton_normal)
        addEdit_RadioButton_Bajo = findViewById(R.id.addEdit_radioButton_baja)
        //


        val forEdit = intent.getBooleanExtra("forEdit", false)

        if (forEdit) {
            val idTask = intent.getStringExtra("idTask")
            val currentTask = AppDatabase.getAppDatabase(this).getTareaDao().getTareaByID(idTask!!)

            addEdit_TextView_Header.text = currentTask.title
        }
    }
}
