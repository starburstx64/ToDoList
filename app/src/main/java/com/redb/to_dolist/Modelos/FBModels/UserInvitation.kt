package com.redb.to_dolist.Modelos.FBModels

data class UserInvitation(
    var idlist:String="",
    var listTitle:String="",
    var accepted:Boolean?=null
)
{
    var id:String?=null
}
