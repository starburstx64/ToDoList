package com.redb.to_dolist.Modelos.FBModels

data class Task (
    var title:String="",
    var importance:Int=0,
    var duedate:Int?=null,
    var completed:Boolean=false,
    var creator:String="",
    var creatorName:String="",
    var creatorIcon:Int=1,
    var description:String=""
)
{
    var id:String?=null
}