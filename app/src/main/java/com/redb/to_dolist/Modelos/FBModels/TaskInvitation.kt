package com.redb.to_dolist.Modelos.FBModels

data class TaskInvitation (
    var idUser:String="",
    var userEmail:String="",
    var state:Boolean?=null
)
{
    var id:String?=null
}