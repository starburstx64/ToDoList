package com.redb.to_dolist.Modelos

data class Tarea(
    val idTarea:String,
    val idLista:String,
    var title:String,
    var importance:Int,
    var dueDate:Int,
    var completed:Boolean,
    val creator:String,
    var creatorName:String,
    var creatorIcon:Int
)