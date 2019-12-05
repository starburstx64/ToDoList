package com.redb.to_dolist

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.redb.to_dolist.DB.AppDatabase
import kotlinx.android.synthetic.main.fragment_add_edit_task.*

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
    lateinit var addEdit_Button_agregar: Button

    private var forEdit = false
    private lateinit var idTask : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_actitvity)

        addEdit_TextView_Header = findViewById(R.id.addEdit_textView_header)
        addEdit_EditText_Descripcion = findViewById(R.id.addEdit_editText_descripcion)
        addEdit_EditText_Nombre = findViewById(R.id.addEdit_editText_nombre)

        //RadioButtons de nivel de importancia
        addEdit_RadioButton_SinAsignar = findViewById(R.id.addEdit_radioButton_sinAsignar)
        addEdit_RadioButton_Alto = findViewById(R.id.addEdit_radioButton_alta)
        addEdit_RadioButton_Medio = findViewById(R.id.addEdit_radioButton_normal)
        addEdit_RadioButton_Bajo = findViewById(R.id.addEdit_radioButton_baja)
        //

        //Date picker
        addEdit_CheckBox_AceptarFecha = findViewById(R.id.addEdit_checkBox_fecha)
        addEdit_DatePicker_fecha = findViewById(R.id.addEdit_datePicker_fecha)
        addEdit_Button_agregar = findViewById(R.id.addEdit_btn_agregar)

        forEdit = intent.getBooleanExtra("forEdit", false)

        if (forEdit) {
            idTask = intent.getStringExtra("idTask")!!
            val currentTask = AppDatabase.getAppDatabase(this).getTareaDao().getTareaByID(idTask!!)

            addEdit_EditText_Nombre.setText(currentTask.title, TextView.BufferType.EDITABLE)
            addEdit_EditText_Descripcion.setText(currentTask.descrition, TextView.BufferType.EDITABLE)
            addEdit_CheckBox_AceptarFecha.isChecked = currentTask.dueDate != 0
            val year = currentTask.dueDate.toString().substring(0, 4).toInt()
            val month = currentTask.dueDate.toString().substring(4, 6).toInt()
            val day = currentTask.dueDate.toString().substring(6, 8).toInt()

            if (year + month + day > 0) {
                addEdit_DatePicker_fecha.visibility = View.VISIBLE
                addEdit_DatePicker_fecha.updateDate(year, month, day)
            }

            when (currentTask.importance) {
                0 -> addEdit_RadioButton_SinAsignar.isChecked = true
                1 -> addEdit_RadioButton_Bajo.isChecked = true
                2 -> addEdit_RadioButton_Medio.isChecked = true
                3 -> addEdit_RadioButton_Alto.isChecked = true
            }

            addEdit_Button_agregar.text = "Editar Tarea"
            addEdit_Button_agregar.setOnClickListener{

                //Modificar la tarea en firebase
                val database = FirebaseDatabase.getInstance()
                val db:AppDatabase= AppDatabase.getAppDatabase(this)
                val listid=db.getAplicacionDao().getAplicationList()

                val taskRef= database.getReference("App").child("tasks").child(listid).child(idTask)

                val año = addEdit_DatePicker_fecha.year
                val mes =
                    if (addEdit_DatePicker_fecha.month.toString().length==1)
                        "0"+ addEdit_DatePicker_fecha.month.toString()
                    else addEdit_DatePicker_fecha.month.toString()
                val dia =
                    if (addEdit_DatePicker_fecha.dayOfMonth.toString().length==1)
                        "0"+ addEdit_DatePicker_fecha.month.toString()
                    else addEdit_DatePicker_fecha.dayOfMonth.toString()
                val date = (año.toString() + mes + dia).toInt()
                if (!addEdit_CheckBox_AceptarFecha.isChecked) {
                    taskRef.child("duedate").setValue(null)
                    currentTask.dueDate=null
                } else {
                    taskRef.child("duedate").setValue(date)
                    currentTask.dueDate=date
                }

                if (addEdit_EditText_Descripcion.text.isBlank()) {
                    taskRef.child("description").setValue("Sin Descripción")
                    currentTask.descrition="Sin descripcion"
                } else {
                    taskRef.child("description")
                        .setValue(addEdit_EditText_Descripcion.text.toString())
                    currentTask.descrition=addEdit_EditText_Descripcion.text.toString()
                }

                taskRef.child("completed").setValue(false)
                taskRef.child("title").setValue(addEdit_EditText_Nombre.text.toString())
                currentTask.title=addEdit_EditText_Nombre.text.toString()
                var importancia =
                    when
                    {
                        addEdit_RadioButton_Bajo.isChecked->1
                        addEdit_RadioButton_Medio.isChecked->2
                        addEdit_RadioButton_Alto.isChecked->3
                        else -> 0
                    }
                currentTask.importance=importancia
                taskRef.child("importance").setValue(currentTask.importance)

                db.getTareaDao().ModificarTarea(currentTask)

                Toast.makeText(this, "La tarea ha sido modificada", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun getCurrentTaskID() : String {
        return idTask
    }

    fun getCurrentList():String
    {
        return "fjdasfj"
    }
}
