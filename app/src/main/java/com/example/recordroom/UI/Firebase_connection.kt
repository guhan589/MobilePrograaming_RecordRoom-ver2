package com.example.recordroom.UI

import com.example.recordroom.model.UserDto
import com.google.firebase.database.*

class Firebase_connection{

    /**
     * 파이어베이스 연동 및 데이터 setter getter
     * **/
    var database : FirebaseDatabase? = null
    var myRef : DatabaseReference?=null
    lateinit var id:String
    lateinit var passwd:String
    lateinit var name:String
    lateinit var email:String

    constructor(){
        setInstance()
    }

    constructor(id:String, passwd: String){
        setInstance()
        this.id = id
        this.passwd = passwd
    }
    constructor(id:String, passwd:String, name:String,email:String){
        setInstance()
        this.id = id
        this.passwd = passwd
        this.name = name
        this.email = email
    }


    fun singupUser():Boolean {//회원가입
        myRef = database!!.getReference("User")
        myRef?.child(id)?.setValue(UserDto(passwd, name, email))

        return true
    }

    fun setInstance(){
        if(database==null)
            database = FirebaseDatabase.getInstance()

        myRef = database!!.getReference()
    }

    fun getInstance(): FirebaseDatabase? {
        return database
    }


}