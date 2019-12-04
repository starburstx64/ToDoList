package com.redb.to_dolist.VistaModelos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuPrincipalVM : ViewModel() {

    private val currentList = MutableLiveData<String>()

    fun setCurrentList(idList : String) {
        currentList.value = idList
    }

    fun getCurrentList() : String {
        return currentList.value!!
    }
}