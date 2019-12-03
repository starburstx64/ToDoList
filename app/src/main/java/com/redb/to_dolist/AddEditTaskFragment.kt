package com.redb.to_dolist

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

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
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val view  = inflater.inflate(R.layout.fragment_add_edit_task, container, false)

        addEdit_TextView_Header = view.findViewById(R.id.addEdit_textView_header)
        addEdit_EditText_Descripcion = view.findViewById(R.id.addEdit_editText_descripcion)
        addEdit_EditText_Nombre = view.findViewById(R.id.addEdit_editText_nombre)
        addEdit_RadioButton_SinAsignar = view.findViewById(R.id.addEdit_radioButton_sinAsignar)
        addEdit_RadioButton_Alto = view.findViewById(R.id.addEdit_radioButton_alta)
        addEdit_RadioButton_Medio = view.findViewById(R.id.addEdit_radioButton_normal)
        addEdit_RadioButton_Bajo = view.findViewById(R.id.addEdit_radioButton_baja)
        addEdit_DatePicker_fecha = view.findViewById(R.id.addEdit_datePicker_fecha)
        addEdit_Button_agregar = view.findViewById(R.id.addEdit_btn_agregar)

        val activeList = arguments?.getString("activeList")

        addEdit_Button_agregar.setOnClickListener{

            val databse  = FirebaseDatabase.getInstance()
            val taskRef = databse.getReference("App").child("tasks").child(activeList!!).push()
            taskRef.child("completed").setValue(false)


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
