package com.redb.to_dolist.Modelos

data class Usuario(
    val idUsuario: String,
    var userName:String,
    var password:String,
    var selectedAvatar:Int,
    val email:String
)
