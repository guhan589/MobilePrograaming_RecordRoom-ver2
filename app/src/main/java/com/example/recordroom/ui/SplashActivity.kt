package com.example.recordroom.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recordroom.R
import com.example.recordroom.ui.login.LoginActivity
import com.example.recordroom.model.SharedUserData
import com.example.recordroom.ui.home.HomeActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        /**
         * 어플 초기 실행 부분
         * **/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        getHashKey();

        val auto_login = SharedUserData(this).getisAuto_login()
        if(auto_login) {//자동로그인 시
            Handler().postDelayed({
                Toast.makeText(this,"자동로그인이 되었습니다.",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)//mainActivity으로 이동
                startActivity(intent)
                finish()
            },2000)

        }else{
            Handler().postDelayed({

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }


    }
    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(
                    "KeyHash",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT)
                )
            } catch (e: NoSuchAlgorithmException) {
                Log.e(
                    "KeyHash",
                    "Unable to get MessageDigest. signature=$signature",
                    e
                )
            }
        }
    }


}