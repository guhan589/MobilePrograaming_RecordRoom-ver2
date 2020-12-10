package com.example.recordroom.ui.commom

import android.net.Uri

data class RoomRecord(var roomName:String ?= null,
                      var address:String ?= null,
                      var latitude:Double ?= null,
                      var longitude:Double ?= null,
                      var imageUri: ArrayList<String> ?= null,
                      var imageName:ArrayList<String> ?= null,
                      var scores: ArrayList<Double> ?= null)  {

    /**
     *  각 방의 정보를 가지고 있는 클래스(제목, 주소, 경도, 위도)
     * **/
}