package com.redb.to_dolist.Modelos.FBModels

data class List (
    var title:String="",
    var description:String="",
    var creator:String="",
    var shared:Boolean=false,
    var backgroundColor:String="red",
    var listIcon:Int=1,
    var users:MutableMap<String,Boolean> = HashMap()
){
    var id:String?=null
}