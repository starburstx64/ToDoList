package com.redb.to_dolist.Vistas.NavigationDrawer.home

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
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.TareaEntity
import com.redb.to_dolist.R

class HomeFragment : Fragment() {

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
                personPhoto.setImageResource(task.creatorIcon)
                tituloTextView.text = task.title
                importanceTextView.text = view.resources.getString(R.string.task_importance, task.importance)
                dueDateTextView.text = view.resources.getString(R.string.task_dueDate, task.dueDate)
                completedCheckBox.isChecked = task.completed
                userNameTextView.text = task.creatorName
                descriptionTextView.text = task.descrition

                linearLayoutRoot.setOnClickListener {
                    // Notificar al activity cambiar el fragmento
                }

                completedCheckBox.setOnCheckedChangeListener {_, isCheked ->
                    if (isCheked) {
                        fragment.removeTask(layoutPosition)
                        Snackbar.make(view, "Tarea Completada", Snackbar.LENGTH_LONG).setAction("Deshacer"
                        ) {
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

    private lateinit var homeViewModel: HomeViewModel

    private val taskList = mutableListOf(TareaEntity("asasd", "asdad",
        "Terminar Proyecto", 3, 20191011, false, "asda",
        "David Ley", R.drawable.foto_01, "Padoru Padoru"), TareaEntity("sdsd", "gfd",
        "Mandar a chinga a su madre", 1, 2020111, false, "sddd",
        "Raul", R.drawable.foto_02, "Hola Rasa"))

    private var deletedTask : TareaEntity? = null
    private var deletedTaskPosition : Int? = null

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var actionBar: ActionBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        taskRecyclerView = view.findViewById<RecyclerView>(R.id.task_recyclerView_tareas).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = Adapter(this@HomeFragment)
        }

        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar.subtitle = "Hola Rasa"

        val roomDatabase = AppDatabase.getAppDatabase(view.context)

        return view
    }

    fun removeTask(position : Int) {
        deletedTask = taskList.removeAt(position)
        deletedTaskPosition = position
        taskRecyclerView.adapter?.notifyDataSetChanged()
    }

    fun restoreTask() {
        taskList.add(deletedTaskPosition!!, deletedTask!!)
        taskRecyclerView.adapter?.notifyDataSetChanged()
    }
}