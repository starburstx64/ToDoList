package com.redb.to_dolist.Modelos

data class Invitacion(
    val idInvitacion:String,
    val idUsuario:String,
    val idLista:String,
    val listTitle:String,
    var accepted:Boolean
)
