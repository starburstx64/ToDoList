package com.redb.to_dolist.VistaModelos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.TareaEntity
import com.redb.to_dolist.R

class MenuPrincipalVM : ViewModel() {

    private val currentList = MutableLiveData<String>()
    private var taskList : MutableLiveData<MutableList<TareaEntity>> = MutableLiveData()
    private var selectedList:String=""
    lateinit var databaseRoom:AppDatabase

    init {
        currentList.value = "12345"

        taskList.value = mutableListOf(TareaEntity("1234", "asdad",
            "Terminar Proyecto", 3, 20191011, false, "asda",
            "David Ley", R.drawable.foto_01, "Padoru Padoru"), TareaEntity("sdsd", "gfd",
            "Mandar a chinga a su madre", 1, 2020111, false, "sddd",
            "Raul", R.drawable.foto_02, "Hola Rasa"), TareaEntity("sdsd", "gfd",
            "El Mama dick", 2, 2010111, false, "sddd",
            "Zeta", R.drawable.foto_03, "El cuatro acordes"))
    }

    fun setCurrentListID(idList : String) {
        currentList.value = idList
    }

    fun setSelectedList(idLista:String)
    {
        selectedList=idLista
    }
    

    fun getCurrentListID() : String {
        return currentList.value!!
    }

    fun setCurrentTaskList(newList : MutableList<TareaEntity>) {
        taskList.value = newList
    }

    fun getCurrentTaskList() : MutableLiveData<MutableList<TareaEntity>> {
        return taskList
    }

    fun sortList(sortByDueDate : Boolean, sortAsc : Boolean) {

        if (sortByDueDate) {
            taskList.value!!.sortWith(object : Comparator<TareaEntity> {
                override fun compare(p0: TareaEntity?, p1: TareaEntity?): Int {

                    if (p0!!.dueDate == null && p1!!.dueDate != null) return 1
                    if (p0.dueDate != null && p1!!.dueDate == null) return -1

                    return when {
                        p0.dueDate == p1!!.dueDate -> 0
                        else -> -1
                    }
                }
            })

            taskList.value = taskList.value // OuO
        }

        else { // Sort by Importance
            taskList.value!!.sortWith(object : Comparator<TareaEntity> {
                override fun compare(p0: TareaEntity?, p1: TareaEntity?): Int {
                    return when {
                        p0!!.importance > p1!!.importance -> 1
                        p0.importance == p1.importance -> 0
                        else -> -1
                    }
                }
            })

            taskList.value = taskList.value
        }

        if (!sortAsc) {
            taskList.value!!.reverse()
        }
    }
}