package com.redb.to_dolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_edit_task.*

class AddEditTaskActivity : FragmentActivity() {

    lateinit var addEdit_TextView_Header : TextView
    lateinit var addEdit_EditText_Nombre : EditText
    lateinit var addEdit_EditText_Descripcion : EditText
    lateinit var addEdit_RadioButton_SinAsignar : RadioButton
    lateinit var addEdit_RadioButton_Alto: RadioButton
    lateinit var addEdit_RadioButton_Medio : RadioButton
    lateinit var addEdit_RadioButton_Bajo : RadioButton
    lateinit var addEdit_DatePicker_fecha : DatePicker
    lateinit var addEdit_Button_agregar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)


        addEdit_TextView_Header = findViewById(R.id.addEdit_textView_header)
        addEdit_EditText_Descripcion = findViewById(R.id.addEdit_editText_descripcion)
        addEdit_EditText_Nombre = findViewById(R.id.addEdit_editText_nombre)
        addEdit_RadioButton_SinAsignar = findViewById(R.id.addEdit_radioButton_sinAsignar)
        addEdit_RadioButton_Alto = findViewById(R.id.addEdit_radioButton_alta)
        addEdit_RadioButton_Medio = findViewById(R.id.addEdit_radioButton_normal)
        addEdit_RadioButton_Bajo = findViewById(R.id.addEdit_radioButton_baja)
        addEdit_DatePicker_fecha = findViewById(R.id.addEdit_datePicker_fecha)
        addEdit_Button_agregar = findViewById(R.id.addEdit_btn_agregar)



        addEdit_Button_agregar.setOnClickListener{
            val databse  = FirebaseDatabase.getInstance()
           // val usersref =
        }






    }
}
