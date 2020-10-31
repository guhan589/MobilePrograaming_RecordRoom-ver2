package com.example.recordroom.Messagepop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.recordroom.R
import com.example.recordroom.SignupActivity
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_signup.*

class MessageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE) //setContentView를 하기전에 작성해야 오류 발생이 되지 않음.
        setContentView(R.layout.activity_message)



        //데이터 가져오기
        val intent = intent
        val state = intent.getBooleanExtra("state",false)
        Log.d("TAG", "onCreate_state: "+state)
        if(state) {
            title_textview.setText("ID사용가능")
            useId_btn.visibility = View.VISIBLE //사용하기 버튼 활성화
        }
        val data = intent.getStringExtra("data")
        txtText!!.text = data

        useId_btn.setOnClickListener{
            val intent = Intent()
            intent.putExtra("result", "ok")
            setResult(RESULT_OK, intent)
            //액티비티(팝업) 닫기
            finish()

        }
    }

    //확인 버튼 클릭
    fun mOnClose(v: View?) {
        //데이터 전달하기
        val intent = Intent()
        intent.putExtra("result", "Close Popup")
        setResult(RESULT_OK, intent)

        //액티비티(팝업) 닫기
        finish()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //바깥레이어 클릭시 안닫히게
        return if (event.action == MotionEvent.ACTION_OUTSIDE) {
            false
        } else true
    }

    override fun onBackPressed() {
        //안드로이드 백버튼 막기
        return
    }

}