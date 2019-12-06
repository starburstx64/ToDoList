package com.redb.to_dolist.Vistas

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.ListaEntity
import com.redb.to_dolist.Modelos.FBModels.User
import com.redb.to_dolist.Modelos.Usuario

import com.redb.to_dolist.R
import com.redb.to_dolist.VistaModelos.MenuPrincipalVM

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddListFragment : Fragment() {

    class Adapter(private val fragment : AddListFragment) : RecyclerView.Adapter<Adapter.AdapterViewHolder>() {

        class AdapterViewHolder(private val view: View, private val fragment: AddListFragment) :
            RecyclerView.ViewHolder(view) {
            private var textViewUserName: TextView =
                view.findViewById(R.id.list_textView_rvUserName)

            fun bind(user: Usuario) {
                textViewUserName.text = user.userName

                view.findViewById<ImageView>(R.id.list_imageView_rvDeleteUserButton)
                    .setOnClickListener {
                        fragment.deleteUserAtPosition(layoutPosition)
                    }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.rv_list_user_list,
                parent,
                false
            ) as View

            return AdapterViewHolder(view, fragment)
        }

        override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
            holder.bind(fragment.userList[position])
        }

        override fun getItemCount(): Int {
            return fragment.userList.size
        }
    }

    class ColorSpinnerAdapter(internal var context: Context, private var colors: IntArray) : BaseAdapter() {

        private var inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return colors.size
        }

        override fun getItem(i: Int): Any? {
            return null
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            val newView = inflater.inflate(R.layout.spiner_color_row, null)
            val icon = newView.findViewById(R.id.spinner_color_imageView) as ImageView
            icon.setBackgroundColor(colors[i])
            return newView
        }
    }

    class IconSpinnerAdapter(internal var context: Context, private var icons: IntArray) : BaseAdapter() {

        private var inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return icons.size
        }

        override fun getItem(i: Int): Any? {
            return null
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            val newView = inflater.inflate(R.layout.spiner_icon_row, null)
            val icon = newView.findViewById(R.id.spinner_icon_imageView) as ImageView
            icon.setImageResource(icons[i])
            return newView
        }
    }



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var listNameEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var descriptionEditText: EditText

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

    private var database = FirebaseDatabase.getInstance()

    private var userList = arrayListOf<Usuario>()

    private val colors = listOf("Red", "Blue", "Yellow", "Green")
    private var selectedColor = 0
    private var selectedIcon = 0
    private lateinit var db:AppDatabase

    private lateinit var model : MenuPrincipalVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        model = ViewModelProviders.of(requireActivity()).get(MenuPrincipalVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_list, container, false)
        db = AppDatabase.getAppDatabase(activity as Context)

        listNameEditText = view.findViewById(R.id.list_edtiText_nameList)
        descriptionEditText = view.findViewById(R.id.list_inputText_description)
        userNameEditText = view.findViewById(R.id.list_editText_userName)

        backgroundColor = view.findViewById(R.id.list_spinner_color)
        icon = view.findViewById(R.id.list_spinner_icono)

        checkShare = view.findViewById(R.id.list_checkBox_listaCompartida)
        addButton = view.findViewById(R.id.list_button_addUser)
        createButton = view.findViewById(R.id.list_button_crearLista)
        cancelButton = view.findViewById(R.id.list_button_cancelarLista)

        checkShare.setOnCheckedChangeListener {_, isCheked ->
            addButton.isEnabled = isCheked
            userNameEditText.isEnabled = isCheked
        }

        val colorsList = listOf(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
        val colorSpinnerAdapter = ColorSpinnerAdapter(view.context, colorsList.toIntArray())
        backgroundColor.adapter = colorSpinnerAdapter
        backgroundColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedColor = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val iconList = listOf(R.drawable.trophy_gren, R.drawable.trophy_white, R.drawable.trophy_yellow)
        val iconSpinnerAdapter = IconSpinnerAdapter(view.context, iconList.toIntArray())
        icon.adapter = iconSpinnerAdapter
        icon.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedIcon = when (p2) {
                    0 -> {
                        1
                    }

                    1 -> {
                        2
                    }

                    2 -> {
                        3
                    }

                    else -> {
                        1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        userRecycler = view.findViewById<RecyclerView>(R.id.list_recyclerView_showUser).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = Adapter(this@AddListFragment)
        }

        addButton.setOnClickListener {
            database.getReference("App").child("users").orderByChild("email").equalTo(userNameEditText.text.toString()).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val userObtained: User? = p0.getValue(User::class.java)

                        p0.children.forEach{
                            userObtained!!.username=it.child("username").value.toString()
                            userObtained.password=it.child("password").value.toString()
                            userObtained.id = it.key
                            userObtained.email=it.child("email").value.toString()
                            userObtained.selectedAvatar=it.child("selectedAvatar").value.toString().toInt()
                            userObtained.confirmed=it.child("confirmed").value.toString().toBoolean()
                        }

                        //val text = userNameEditText.text.toString().filterNot { c -> "@.".contains(c) }

                        if (userObtained?.username!="" && p0.exists()) {

                            userList.add(Usuario(userObtained!!.id.toString(),userObtained.username,"",userObtained.selectedAvatar,userObtained.email))
                            userRecycler.adapter?.notifyDataSetChanged()

                            userNameEditText.text.clear()
                        }

                        else {
                            val dialog = AlertDialog.Builder(view.context)
                            dialog.setMessage("No se encontro un usuario para el correo seleccionado")
                            dialog.setTitle("Correo no encontrado")
                            dialog.setPositiveButton("Ok") { _, _ ->

                            }

                            val alertDialog = dialog.create()
                            alertDialog.show()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
        }

        createButton.setOnClickListener {

            if (listNameEditText.text.isBlank()) {
                val dialog = AlertDialog.Builder(view.context)
                dialog.setMessage("Favor de llenar todos los campos")
                dialog.setTitle("Campos Incompletos")
                dialog.setPositiveButton("Ok") { _, _ ->

                }

                val alertDialog = dialog.create()
                alertDialog.show()
            }

            else {
                val loggedUser = AppDatabase.getAppDatabase(view.context).getAplicacionDao().getLoggedUser()

                var listKey:String =""
                //val listsRef = database.getReference("App").child("lists").child(listKey.toString())
                val listsRef : DatabaseReference = if ((activity as? ActivityList) != null) {
                    listKey = db.getAplicacionDao().getAplicationList()
                    database.getReference("App").child("lists").child(listKey)
                } else {
                    listKey = database.getReference("App").child("lists").push().key.toString()
                    database.getReference("App").child("lists").child(listKey)
                }

                listsRef.child("creator").setValue(loggedUser!!)
                listsRef.child("shared").setValue(checkShare.isChecked)
                listsRef.child("backgroundColor").setValue(colors[selectedColor])
                listsRef.child("title").setValue(listNameEditText.text.toString())
                listsRef.child("description").setValue(descriptionEditText.text.toString())
                listsRef.child("listIcon").setValue(selectedIcon)
                listsRef.child("users").child(loggedUser).setValue(true)

                database.getReference("App").child("users").child(loggedUser).child("lists").child(listKey.toString()).setValue(true)

                val userInvitationsRef = database.getReference("App").child("userInvitations")
                userList.forEach {
                    listsRef.child("users").child(it.idUsuario).setValue(true)

                    val invitationRef = userInvitationsRef.child(it.idUsuario).push()
                    invitationRef.child("idList").setValue(listsRef.key)
                    invitationRef.child("listTitle").setValue(listNameEditText.text.toString())

                    val listInvitationsRef = database.getReference("App").child("listInvitations").child(listsRef.key!!).child(invitationRef.key!!)
                    listInvitationsRef.child("idUser").setValue(it.idUsuario)
                    listInvitationsRef.child("userEmail").setValue(it.email)
                    listInvitationsRef.child("state").setValue(null)
                }

                /*val roomDatabase = AppDatabase.getAppDatabase(view.context)

                val user = roomDatabase.getUsuarioDao().getUsuarioByID(loggedUser)
                val listToInsert = ListaEntity(listsRef.key!!, loggedUser,
                    listNameEditText.text.toString(), descriptionEditText.text.toString(),
                    loggedUser.toString(), user.username, checkShare.isChecked, selectedIcon, colors[selectedColor])

                roomDatabase.getListaDao().insertList(listToInsert)*/

                Toast.makeText(view.context, "Lista Creada Correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            activity?.onBackPressed()
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
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
         * @return A new instance of fragment AddListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun deleteUserAtPosition(position : Int) {
        userList.removeAt(position)
        userRecycler.adapter?.notifyDataSetChanged()
    }
}
