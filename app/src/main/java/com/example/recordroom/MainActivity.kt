package com.example.recordroom

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.recordroom.model.SharedUserData
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.appbar.view.*


class MainActivity : AppCompatActivity() {
    var data = arrayListOf<RoomRecord>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val name = intent.getStringExtra("name")
        Log.d("TAG", "name: "+name)
        val toolbar:Toolbar
        toolbar = appbar
        toolbar.appbar_textView.setText("방구")
        toolbar.setTitle("")
        setSupportActionBar(toolbar)

        data.add(RoomRecord("a","b","c","d"))
        val adapter = ListAdapter(this,data)
        adapter.notifyDataSetChanged()
        listView.adapter = adapter

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //menu xml 설정
        menuInflater.inflate(R.menu.appbarmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//menu 이벤트 처리

        when(item.itemId){
            R.id.logout_Btn->{//로그아웃 버튼 누를시
                logout_dialog()
                /*SharedUserData(this).reset()
                finish()//엑티비티 종료*/
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun logout_dialog() {
        val alertdialog =  AlertDialog.Builder(this)
        alertdialog.setCancelable(false) //외부영역 터치시 dismiss되는것을 방지
        alertdialog.setMessage("현재 계정을 종료하시겠습니까?")
        alertdialog.setPositiveButton("네",
            DialogInterface.OnClickListener { dialog, which ->
                SharedUserData(this).reset() //로그아웃시 사용자의 자동로그인을 해제하기위해 reset 메소드 실행
                finish()

            })
        alertdialog.setNegativeButton("취소",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        val alert: AlertDialog = alertdialog.create()
        alert.show()
    }


}