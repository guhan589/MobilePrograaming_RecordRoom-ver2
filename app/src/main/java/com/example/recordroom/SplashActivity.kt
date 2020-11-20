package com.example.recordroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.recordroom.model.SharedUserData

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val auto_login = SharedUserData(this).getisAuto_login()
        if(auto_login) {//자동로그인 시
            Handler().postDelayed({
                Toast.makeText(this,"자동로그인이 되었습니다.",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)//mainActivity으로 이동
                startActivity(intent)
                finish()
            },2000)

        }else{
            Handler().postDelayed({

                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }


    }


}