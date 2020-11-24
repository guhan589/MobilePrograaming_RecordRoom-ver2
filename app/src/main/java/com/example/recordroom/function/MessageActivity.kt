package com.example.recordroom.function

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import com.example.recordroom.R
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : Activity() { //아이디 중복 검사시 사용가능 여부 메시지를 팝업형태로 띄움

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE) //setContentView를 하기전에 작성해야 오류 발생이 되지 않음.
        setContentView(R.layout.activity_message)



        //데이터 가져오기
        val intent = intent
        val state = intent.getBooleanExtra("state",false)
        Log.d("TAG", "onCreate_state: "+state)
        if(state) { //사용 가능한 ID일시 메시지
            title_textview.setText("ID사용가능")
            useId_btn.visibility = View.VISIBLE //사용하기 버튼 활성화
            //cancel_Btn.visibility = View.GONE //취소버튼 비활성화
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