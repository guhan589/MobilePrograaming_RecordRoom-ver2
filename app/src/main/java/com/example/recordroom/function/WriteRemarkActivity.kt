package com.example.recordroom.function

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import com.example.recordroom.R
import kotlinx.android.synthetic.main.activity_write_remark.*

class WriteRemarkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //setContentView를 하기전에 작성해야 오류 발생이 되지 않음.
        setContentView(R.layout.activity_write_remark)

    }

    //확인 버튼 클릭
    fun mOnOk(v: View?) {
        //데이터 전달하기
        val intent = Intent()
        intent.putExtra("result", editText.text)
        setResult(Activity.RESULT_OK, intent)

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
        super.onBackPressed()
    }

}