package com.example.recordroom.model

data class RoomInform(var roomName : String? = null,
                      var address : String? = null,
                      var scores : ArrayList<Float>? = null,
                      var imageUrl : String? = null,
                        var imagename:String?=null){
}
