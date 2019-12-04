package com.redb.to_dolist.VistaModelos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redb.to_dolist.DB.Entidades.TareaEntity

class MenuPrincipalVM : ViewModel() {

    private val currentList = MutableLiveData<String>()

    fun setCurrentListID(idList : String) {
        currentList.value = idList
    }

    fun getCurrentListID() : String {
        return currentList.value!!
    }

    fun getCurrentTaskList() : List<TareaEntity> {
        return listOf()
    }
}