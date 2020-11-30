package com.example.recordroom.model

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log

public class SharedUserData {
    
    /**
     * 사용자의 계정 정보 값을 xml에 저장하기 위한 클래스
     * 
     * */
    private var activity: Activity? = null
    var loginInformtaion: SharedPreferences? = null
    var editor: Editor? = null

    constructor(activity: Activity?) {
        this.activity = activity
    }


    fun setUserId(user_id: String?, store_id: Boolean) { //아이디 저장 메소드
        loginInformtaion =
            activity?.getSharedPreferences("user_inform", Context.MODE_PRIVATE)
        editor = loginInformtaion?.edit()
        editor?.putString("user_id", user_id)
        editor?.putBoolean("stored_id", store_id)
        editor?.apply()
    }

    fun setUserAuto(id: String, password: String, auto_login: Boolean) { //자동로그인 메소드

        Log.d("TAG", "setUserAuto: id$id")
        Log.d("TAG", "setUserAuto: password$password")
        Log.d("TAG", "setUserAuto: auto_login$auto_login")
        loginInformtaion =
            activity?.getSharedPreferences("user_inform", Context.MODE_PRIVATE)
        editor = loginInformtaion?.edit()
        editor?.putString("user_id", id)
        editor?.putString("user_password", password)
        editor?.putBoolean("auto_login", auto_login)
        editor?.apply()
    }

    fun getUser_id(): String? {
        loginInformtaion =
            activity!!.getSharedPreferences("user_inform", Context.MODE_PRIVATE)
        return loginInformtaion?.getString("user_id", "")
    }

    fun getUser_password(): String? {
        loginInformtaion =
            activity!!.getSharedPreferences("user_inform", Context.MODE_PRIVATE)
        return loginInformtaion?.getString("user_password", "")
    }

    fun getisStroe_id(): Boolean {
        loginInformtaion =
            activity!!.getSharedPreferences("user_inform", Context.MODE_PRIVATE)
        return loginInformtaion?.getBoolean("stored_id", false)!!
    }

    fun getisAuto_login(): Boolean {
        loginInformtaion =
            activity!!.getSharedPreferences("user_inform", Context.MODE_PRIVATE)
        return loginInformtaion?.getBoolean("auto_login", false)!!
    }

    fun reset() {
        loginInformtaion =
            activity!!.getSharedPreferences("user_inform", Context.MODE_PRIVATE)
        editor = loginInformtaion?.edit()
        editor?.clear()?.apply()
    }

}