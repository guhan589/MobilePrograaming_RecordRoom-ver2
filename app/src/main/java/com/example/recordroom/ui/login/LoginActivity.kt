package com.example.recordroom.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recordroom.R
import com.example.recordroom.ui.commom.Firebase_connection
import com.example.recordroom.ui.home.HomeActivity
import com.example.recordroom.model.SharedUserData
import com.example.recordroom.ui.finduser.FinduserActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*


/**
 * 사용자 로그인 시도
 * 사용자 회원가입 및 회원 정보 찾기(아이디, 비밀번호)
 *
 * **/
public class LoginActivity : AppCompatActivity() {

    private lateinit var user_id:String
    private lateinit var user_pw:String

    private lateinit var sharedUserData: SharedUserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val stored_id = SharedUserData(this).getisStroe_id()
        if(stored_id) {
            ed_userId.setText(SharedUserData(this).getUser_id())//아이디 필드란에 저장된 id값 띄우기
            storeid_btn.isChecked=true
        }

        login_btn.setOnClickListener{//사용자가 로그인 버튼을 누를시 활성화
            user_id = ed_userId.text.toString()
            user_pw = ed_userPw.text.toString()

            val status = search_space(user_id,user_pw)// 아이디와 비밀번호가 모두 입력되었는지 확인
            if(status)//아이디 비밀번호 입력이 모두 입력하였을 경우
                login(user_id,user_pw)

        }
        singupTextview.setOnClickListener{//회원가입 버튼
            val singupIntent = Intent(this, SignupActivity::class.java)
            startActivity(singupIntent)
        }

        findTextView.setOnClickListener{//아이디 비빌번호찾기
            startActivity(Intent(this,FinduserActivity::class.java))
        }

        autologin_btn.setOnClickListener{//자동로그인 체크박스 이벤트처리
            if(autologin_btn.isChecked){
                storeid_btn.isChecked=false
            }
        }

        storeid_btn.setOnClickListener{
            if(storeid_btn.isChecked){
                if(autologin_btn.isChecked) {
                    show("자동로그인이 선택된 경우 아이디 저장이 불가합니다.")
                    storeid_btn.isChecked = false
                }

            }
        }



    }



    fun search_space(id:String, password:String):Boolean { //아이디 비밀번호 공백확인

        var isState:Boolean = false
        if (id.length == 0 || password.length == 0) {

            if (id.length == 0 && password.length == 0) {//아이디 비빌번호 모두 입력하지 않았을경우
                show("아이디 및 비밀번호를 확인 후 다시 입력하세요.")
            } else if (id.length == 0) {
                show("아이디를 확인하고 다시 입력하세요.")
            } else {
                show("비밀번호를 확인하고 다시 입력하세요.")
            }
            isState = false
        }
        else
            isState= true


        return isState

    }

    fun login(id:String, pwd:String) {
        var name=""
        val database = Firebase_connection().getInstance()
        val table_user: DatabaseReference? = database?.getReference()?.child("User")
        table_user?.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var status:Boolean = false //첫번쨰 for문을 break하기 위함
                var intentState = false//계정 유무에 따라 로그인이 가능/불가능으로 나뉨
                for(snapshot1 in snapshot.children){

                    if(id.equals(snapshot1.key)){ //사용자가 입력한 id와 같은 key가 있을경우

                        for(snapshot2 in snapshot1.children) {


                            if (snapshot2.key.equals("passwd")) {
                                if(snapshot2.value?.equals(pwd)!!){
                                    name = snapshot1.child("userName").value.toString()//사용자 이름
                                    intentState = true // intent 상태 유무
                                    status = true //이중 for문 중지
                                    break
                                }
                            }
                        }
                    }
                    if(status)//아이디와 비밀번호가 맞을경우 key값 반복문을 멈춘다
                        break
                }

                if(intentState){//계정이 있을경우 MainActivity으로 이동
                    show("로그인 승인이 되었습니다.")
                    if(autologin_btn.isChecked){//자동로그인
                        SharedUserData(this@LoginActivity)
                            .setUserAuto(id,pwd,true)//아이디,패스워드, 상태저장


                    }else if(storeid_btn.isChecked){
                        SharedUserData(this@LoginActivity)
                            .setUserStore(id,true) //아이디와 상태 저장

                    }else
                        SharedUserData(this@LoginActivity)
                            .setUserId(id) // 상태값 없음
                    
                    val mainIntent = Intent(applicationContext, HomeActivity::class.java)
                    Log.d("TAG", "onDataChange_name: "+name)
                    mainIntent.putExtra("name",name)
                    startActivity(mainIntent)
                    finish()
                }else
                    show("계정이 존재하지 않습니다.")


            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
    fun show(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}