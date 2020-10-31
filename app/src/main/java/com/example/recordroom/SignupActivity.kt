package com.example.recordroom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recordroom.Messagepop.MessageActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    lateinit var userId:String//사용자 id
    lateinit var user_password:String //사용자 패스워드
    lateinit var user_name:String //사용자 이름
    lateinit var user_email:String //사용자 이메일
    lateinit var user_mailad:String// 사용자 이메일 도메인
    val REQUEST_TEST = 1
    var singupactivity: Activity = this
    var searchId_state = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        searchId_btn.setOnClickListener{ //중복ID 버튼
            //searchId_state =true//중복로그인 상태 유무
            userId = ed_id.text.toString()
            search_id(userId)
        }
        singup_btn.setOnClickListener{ //회원가입 버튼

            if(ed_id.isEnabled == false)
                searchId_state = true
            val result = search_space()
            Log.d("TAG", "resultresult: "+result)
            if(result){

                user_password = ed_password1.text.toString()
                user_name = ed_name.text.toString()
                user_email =ed_email.text.toString()
                user_email += "@$user_mailad"
                
                val database = Firebase_connection(userId,user_password,user_name,user_email)
                val result = database.singupUser()
                if(result)
                    finish()
            }
        }
        addressSpinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
               
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when(addressSpinner.getItemAtPosition(position)){
                    "naver.com"->{
                        user_mailad = "naver.com"
                    }
                    "daum.net"->{
                        user_mailad = "daum.net"
                    }
                    "google.com" ->{
                        user_mailad = "google.com"
                    }
                    "gwnu.ac.kr" ->{
                        user_mailad ="gwnu.ac.kr"
                    }
                }
            }

        }

    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                val value = data?.getStringExtra("result")
                if(value.equals("ok")){
                    ed_id.isEnabled = false
                }
                //show("Result: " + data?.getStringExtra("result"))
            } else {   // RESULT_CANCEL
                //show("Failed")
            }

        }



    }
    fun search_space():Boolean{ //모든정보를 입력했는지 확인
        var state = false
        userId = ed_id.text.toString() //사용자 아이디
        user_password = ed_password1.text.toString()//사용자 비밀번호
        val user_password2 = ed_password1.text.toString()//사용자 비밀번호 확인
        user_name = ed_name.text.toString()//사용자 이름
        user_email = ed_email.text.toString() //사용자 이메일


        Log.d("TAG", "searchId_statesearchId_state: "+searchId_state)
        if(!searchId_state){
            show("아이디 중복확인을 눌러주세요.")
            return false
        }
        if(userId.equals("") || user_password.equals("") || user_password2.equals("") || user_name.equals("")){
            show("개인정보 모두 입력하세요.")
        }else{
            if(user_password.equals(user_password2)) {//비밀번호와 비밀번호 재확인이 가능한지
                state = true
            }else
                show("비밀번호 재입력란을 다시 입력하세요.")
        }


        return state
    }
    fun search_id(id:String){
        val database = Firebase_connection().getInstance()
        val table_user: DatabaseReference? = database?.getReference()?.child("User")
        table_user?.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var status:Boolean = false //첫번쨰 for문을 break하기 위함
                for(snapshot1 in snapshot.children){
                    Log.d("TAG", "snapshot1: "+snapshot1)
                    if(id.equals(snapshot1.key)){ //사용자가 입력한 id와 같은 key가 있을경우
                        status = true //중복아이디 발견
                        break
                    }
                }


                Log.d("TAG", "statusstatus: "+status)

                if(status){ //중복ID 발생시
                    val intent = Intent(applicationContext, MessageActivity::class.java)
                    intent.putExtra("data", " 다른 사용자가 이용중입니다. \n\n 다른 아이디를 입력해주세요.")
                    startActivityForResult(intent, 1)
                }else{
                    val intent = Intent(applicationContext, MessageActivity::class.java)
                    intent.putExtra("state",!status)
                    intent.putExtra("data", " 사용가능한 ID입니다. \n\n 사용하시겠습니까?")
                    startActivityForResult(intent, 1)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    fun show(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

}