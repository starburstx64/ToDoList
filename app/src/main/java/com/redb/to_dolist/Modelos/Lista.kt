package com.redb.to_dolist.Modelos

data class Lista (
    val idLista:String,
    val idUsuario:String,
    var title:String,
    var description:String,
    val creator:String,
    var creatorName:String,
    val shared:Boolean,
    var backgroundColor:String,
    var listIcon:Int
)
