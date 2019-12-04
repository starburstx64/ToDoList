package com.redb.to_dolist.Modelos.FBModels

data class User(
    var username:String="",
    var email:String="",
    var password:String="",
    var selectedAvatar:Int=1,
    var confirmed:Boolean=false,
    var lists:MutableMap<String,Boolean> = HashMap()
)
{
    var id:String? = null

}