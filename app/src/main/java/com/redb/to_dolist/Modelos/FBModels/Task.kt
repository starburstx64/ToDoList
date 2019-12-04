package com.redb.to_dolist.Modelos.FBModels

data class Task (
    var title:String="",
    var importance:Int=0,
    var duedate:Int=20000101,
    var completed:Boolean=false,
    var creator:String=""
)
{
    var id:String?=null
}