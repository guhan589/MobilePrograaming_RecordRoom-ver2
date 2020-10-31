package com.example.recordroom

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
public class UserDto {
    var userName: String? = null
    var email: String? = null
    var passwd:String?=null
    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(passwd:String,userName: String?, email: String?) {
        this.passwd = passwd
        this.userName = userName
        this.email = email
    }


    override fun toString(): String {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}'
    }
}