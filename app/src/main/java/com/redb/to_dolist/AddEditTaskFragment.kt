package com.redb.to_dolist

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.FirebaseDatabase
import com.redb.to_dolist.DB.AppDatabase
import com.google.firebase.database.DatabaseReference
import com.redb.to_dolist.VistaModelos.MenuPrincipalVM


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddEditTaskFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddEditTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEditTaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
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

    private var prioridad = 0
    private var agregar_fecha = false

    private lateinit var db:AppDatabase


    // fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    //     if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
    //         val p = view!!.parent
    //         if (p != null)
    //             p!!.requestDisallowInterceptTouchEvent(true)
    //     }

    //     return false
    // }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var model: MenuPrincipalVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_edit_task, container, false)


        db= AppDatabase.getAppDatabase(activity as Context)
        addEdit_TextView_Header = view.findViewById(R.id.addEdit_textView_header)
        addEdit_EditText_Descripcion = view.findViewById(R.id.addEdit_editText_descripcion)
        addEdit_EditText_Nombre = view.findViewById(R.id.addEdit_editText_nombre)

        //RadioButtons de nivel de importancia
        addEdit_RadioButton_SinAsignar = view.findViewById(R.id.addEdit_radioButton_sinAsignar)
        addEdit_RadioButton_Alto = view.findViewById(R.id.addEdit_radioButton_alta)
        addEdit_RadioButton_Medio = view.findViewById(R.id.addEdit_radioButton_normal)
        addEdit_RadioButton_Bajo = view.findViewById(R.id.addEdit_radioButton_baja)
        //

        //Date picker
        addEdit_CheckBox_AceptarFecha = view.findViewById(R.id.addEdit_checkBox_fecha)
        addEdit_DatePicker_fecha = view.findViewById(R.id.addEdit_datePicker_fecha)
        addEdit_Button_agregar = view.findViewById(R.id.addEdit_btn_agregar)
        //

        model = ViewModelProviders.of(activity!!).get(MenuPrincipalVM::class.java)
        //db=model.databaseRoom

        addEdit_RadioButton_SinAsignar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prioridad = 0
            }
        }

        addEdit_RadioButton_Bajo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prioridad = 1
            }
        }

        addEdit_RadioButton_Medio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prioridad = 2
            }
        }

        addEdit_RadioButton_Alto.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prioridad = 3
            }
        }

        addEdit_CheckBox_AceptarFecha.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                addEdit_DatePicker_fecha.visibility = View.GONE
            } else {
                addEdit_DatePicker_fecha.visibility = View.VISIBLE

            }

        }


        val activeList = (activity as AddEditTaskActivity).getCurrentList()

        addEdit_Button_agregar.setOnClickListener {

            val logedUser =
                AppDatabase.getAppDatabase(view.context).getAplicacionDao().getLoggedUser()
            val year = addEdit_DatePicker_fecha.year
            val month = 1 + addEdit_DatePicker_fecha.month
            val day = addEdit_DatePicker_fecha.dayOfMonth

            //  val calendar = Calendar.getInstance()

            var mes = month.toString()
            var dia = day.toString()

            if (month < 10) {


                mes = "0" + mes
            }
            if (day < 10) {


                dia = "0" + dia
            }

            // calendar.set(year, month, day)
            val date = year.toString() + mes + dia
            val database = FirebaseDatabase.getInstance()

            if (addEdit_EditText_Nombre.text.isBlank()) {


                Toast.makeText(
                    view.context,
                    "No puedes crear una tarea sin titulo",
                    Toast.LENGTH_SHORT

                ).show()

            } else {
                /*val taskRef : DatabaseReference
                val listid=db.getAplicacionDao().getAplicationList()

                if ((activity as AddEditTaskActivity).forEdit()) {
                    taskRef = database.getReference("App").child("tasks").child(listid).child((activity as AddEditTaskActivity).getCurrentTaskID())
                }

                else {

                    taskRef = database.getReference("App").child("tasks").child(listid).push()*/

                val taskRef : DatabaseReference = if ((activity as? AddEditTaskActivity) != null) {
                    database.getReference("App").child("tasks").child(activeList).child((activity as AddEditTaskActivity).getCurrentTaskID())
                } else {
                    database.getReference("App").child("tasks").child(activeList).push()
                }

                if (!addEdit_CheckBox_AceptarFecha.isChecked) {
                    taskRef.child("duedate").setValue("0")
                } else {
                    taskRef.child("duedate").setValue(date.toString())

                }

                if (addEdit_EditText_Descripcion.text.isBlank()) {
                    taskRef.child("description").setValue("Sin Descripción")
                } else {
                    taskRef.child("description")
                        .setValue(addEdit_EditText_Descripcion.text.toString())
                }

                taskRef.child("completed").setValue(false)
                taskRef.child("creator").setValue(logedUser.toString())
                taskRef.child("title").setValue(addEdit_EditText_Nombre.text.toString())
                taskRef.child("importance").setValue(prioridad)


                Toast.makeText(view.context, "La tarea ha sido agregada con exito", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            // throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEditTaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEditTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
