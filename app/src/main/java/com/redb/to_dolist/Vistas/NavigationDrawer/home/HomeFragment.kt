package com.redb.to_dolist.Vistas.NavigationDrawer.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.redb.to_dolist.AddEditTaskActivity
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.TareaEntity
import com.redb.to_dolist.R
import com.redb.to_dolist.VistaModelos.MenuPrincipalVM

class HomeFragment : Fragment() {

    private val database:FirebaseDatabase = FirebaseDatabase.getInstance()

    class Adapter(private val fragment: HomeFragment) : RecyclerView.Adapter<Adapter.AdapterViewHolder>() {

        class AdapterViewHolder(private val view : View, private val fragment: HomeFragment) : RecyclerView.ViewHolder(view) {

            private var linearLayoutRoot = view.findViewById<LinearLayout>(R.id.task_linerLayout_root)
            private var personPhoto = view.findViewById<ImageView>(R.id.person_photo)
            private var tituloTextView = view.findViewById<TextView>(R.id.task_textView_titulo)
            private var importanceTextView = view.findViewById<TextView>(R.id.task_TextView_importancia)
            private var dueDateTextView = view.findViewById<TextView>(R.id.task_TextView_fechaVencimiento)
            private var completedCheckBox = view.findViewById<CheckBox>(R.id.task_checkBox_completed)
            private var userNameTextView = view.findViewById<TextView>(R.id.task_TextView_userName)
            private var descriptionTextView = view.findViewById<TextView>(R.id.task_textView_description)

            fun bind(task : TareaEntity) {
                //personPhoto.setImageResource(task.creatorIcon)
                tituloTextView.text = task.title
                importanceTextView.text = view.resources.getString(R.string.task_importance, task.importance)
                dueDateTextView.text = view.resources.getString(R.string.task_dueDate, task.dueDate)
                completedCheckBox.isChecked = task.completed
                userNameTextView.text = task.creatorName
                descriptionTextView.text = task.descrition

                linearLayoutRoot.setOnClickListener {
                    val toEditTaskActivity = Intent(fragment.activity, AddEditTaskActivity::class.java)
                    toEditTaskActivity.putExtra("forEdit", true)
                    toEditTaskActivity.putExtra("idTask", task.idTarea)
                    fragment.startActivity(toEditTaskActivity)
                }

                completedCheckBox.setOnCheckedChangeListener {_, isCheked ->
                    if (isCheked) {
                        fragment.removeTask(layoutPosition)
                        val db= AppDatabase.getAppDatabase(fragment.activity as Context)
                        db.getTareaDao().completarTarea(task.idTarea)
                        fragment.database.getReference("App").child("tasks").child(task.idLista).child(task.idTarea).child("completed").setValue(true)

                        Snackbar.make(view, "Tarea Completada", Snackbar.LENGTH_LONG).setAction("Deshacer"
                        ) {
                            db.getTareaDao().descompletarTarea(task.idTarea)
                            fragment.database.getReference("App").child("tasks").child(task.idLista).child(task.idTarea).child("completed").setValue(false)
                            fragment.restoreTask()
                        }.show()
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.rv_tareas_holder,
                parent,
                false
            ) as View

            return AdapterViewHolder(view, fragment)
        }

        override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
            holder.bind(fragment.taskList[position])
        }

        override fun getItemCount(): Int {
            return fragment.taskList.size
        }
    }

    private lateinit var model: MenuPrincipalVM

    private lateinit var  taskList : MutableList<TareaEntity>

    private var deletedTask : TareaEntity? = null
    private var deletedTaskPosition : Int? = null

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var actionBar: ActionBar
    private lateinit var fab : FloatingActionButton
    private lateinit var taskRoot : CoordinatorLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        taskRecyclerView = view.findViewById<RecyclerView>(R.id.task_recyclerView_tareas).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = Adapter(this@HomeFragment)
        }

        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar.subtitle = "Hola Rasa"

        fab = view.findViewById(R.id.task_floatingActionButton_add)
        val db = AppDatabase.getAppDatabase(view.context)
        val listaActual = db.getAplicacionDao().getAplicationList()
        when(listaActual)
        {
            "Todas","Planeadas","Importantes"->fab.isVisible=false
            else->fab.isVisible=true
        }
        fab.setOnClickListener {
            val toAddTaskActivity = Intent(view.context, AddEditTaskActivity::class.java)
            startActivity(toAddTaskActivity)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProviders.of(activity!!).get(MenuPrincipalVM::class.java)
        model.getCurrentTaskList().observe(requireActivity(), object : Observer<MutableList<TareaEntity>>{
            override fun onChanged(t: MutableList<TareaEntity>?) {
                taskList = t!!
                taskRecyclerView.adapter?.notifyDataSetChanged()
            }
        })

        val roomDatabase = AppDatabase.getAppDatabase(view.context)
        val currentList = roomDatabase.getListaDao().getListByID(roomDatabase.getAplicacionDao().getAplicationList())
        taskRoot = view.findViewById(R.id.task_coordinator_root)

        if(currentList!=null) {

            val backgroundColor = when (currentList.backgroundColor) {
                "Red" -> {
                    R.color.red
                }

                "Blue" -> {
                    R.color.blue
                }

                "Yellow" -> {
                    R.color.yellow
                }

                "Green" -> {
                    R.color.green
                }

                else -> {
                    R.color.white
                }
            }

            taskRoot.setBackgroundResource(backgroundColor)
        }
    }

    fun removeTask(position : Int) {
        deletedTask = taskList.removeAt(position)
        deletedTaskPosition = position
        model.setCurrentTaskList(taskList)
    }

    fun restoreTask() {
        taskList.add(deletedTaskPosition!!, deletedTask!!)
        model.setCurrentTaskList(taskList)
    }
}