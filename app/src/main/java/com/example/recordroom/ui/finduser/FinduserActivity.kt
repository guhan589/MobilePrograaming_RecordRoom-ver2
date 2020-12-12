package com.example.recordroom.ui.finduser

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.recordroom.R
import com.example.recordroom.model.SharedUserData
import com.example.recordroom.ui.commom.Firebase_connection
import com.example.recordroom.ui.home.HomeActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_finduser_id.*
import kotlinx.android.synthetic.main.activity_login.*

class FinduserActivity : AppCompatActivity() {
    var userId=""
    var userName=""
    var userEmail = ""
    var result = false
    var database:FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finduser_id)


        idSendBtn.setOnClickListener{//아이디 찾기 버튼 클릭
            userName = usernameEditText1.text.toString() //아이디 찾기 이름
            userEmail = useremailEditText1.text.toString() // 아이디 찾기 이메일
            result = checkFindid(userName,userEmail)
            
            if(result){//이름 이메일를 옳바르게 모두 일력시
                findId(userName,userEmail,resultTextview1)
            }
        }
        passwdSendBtn.setOnClickListener{//비밀번호 찾기 버튼 클릭
            userId = userIdEditText.text.toString()
            userName = usernameEditText2.text.toString()
            userEmail = useremailEditText2.text.toString()

            result = checkFindpwd(userId,userName,userEmail)

            if(result){
                findPwd(userId,userName,userEmail,resultTextview2)

            }
        }
    }

    fun checkFindid(name:String, email:String):Boolean{
        var result = true
        if(name.equals("") || email.equals("") || !email.contains("@")){
            if(name.equals("")){
                show("이름을 입력하세요.")
                result = false
            }else if(email.equals("") || !email.contains("@")){

                if(email.equals(""))
                    show("이메일를 입력하세요.")
                else
                    show("이메일 형식에 맞게 입력하세요.")
                result = false
            }else{
                show("아이디와 이메일을 입력하세요.")
                result = false
            }
        }

        return result
    }
    fun checkFindpwd(userid:String ,name:String, email:String):Boolean{
        var result = true
        if(userid.equals("") || name.equals("") || email.equals("") || !email.contains("@")){
            if(userid.equals("")){
                show("아이디를 입력하세요.")
                result = false
            }else if(name.equals("")){
                show("이름을 입력하세요.")
                result = false
            }else if(email.equals("") || !email.contains("@")){
                if(email.equals(""))
                    show("이메일를 입력하세요.")
                else
                    show("이메일 형식에 맞게 입력하세요.")
                result = false
            }else{
                show("아이디와 이메일을 입력하세요.")
                result = false
            }
        }

        return result
    }
    fun findId(name:String, email:String, resultTextview:TextView){
        var count =0;
        var temp_id = ""
        if(database == null)
            database = Firebase_connection().getInstance()!!

        val tableId = database!!.getReference().child("User")
        tableId.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(temp in snapshot.children){
                    Log.d("TAG", "temp1: $temp")
                    temp_id = temp.key.toString() // 사용자 아이디
                    for(temp2 in temp.children){
                        if(temp2.key.equals("email")){ //이메일 equals
                            if(temp2.value!!.equals(email))
                                count++
                        }else if(temp2.key.equals("userName")){ //사용자 이름 equals
                            if(temp2.value!!.equals(name))
                                count++
                        }

                    }
                    Log.d("TAG", "count1: $count")

                    if(count == 2) {
                        Log.d("TAG", "onDataChange: ")
                        resultTextview.setText("사용자의 아이디는 $temp_id 입니다.")
                        resultTextview.visibility = View.VISIBLE
                        break
                    }
                    count = 0
                }
                if(count != 2){
                    resultTextview.setText("검색하신 정보에 대한 아이디가 없습니다.")
                    resultTextview.visibility = View.VISIBLE
                }
               



            }
            override fun onCancelled(error: DatabaseError) {

            }




        })


    }

    fun findPwd(id:String, name:String, email:String, resultTextview:TextView){
        var count =0;
        var temp_passwd = ""
        if(database == null)
            database = Firebase_connection().getInstance()!!

        val tableId = database!!.getReference().child("User")
        tableId.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(temp in snapshot.children){
                    Log.d("TAG", "temp3: $temp")

                    if(temp.key.equals(id)){
                        count++
                        Log.d("TAG", "temp4: ${temp.key}")
                        for(temp2 in temp.children){
                            if(temp2.key.equals("userName")){
                                if(temp2.value!!.equals(name))
                                    count++
                            }else if(temp2.key.equals("passwd")){
                                temp_passwd = temp2.value.toString()
                            }else if(temp2.key.equals("email")){
                                if(temp2.value!!.equals(email))
                                    count++
                            }
                        }
                        if(count == 3) {
                            resultTextview.setText("사용자의 비밀번호는 $temp_passwd 입니다.")
                            resultTextview.visibility = View.VISIBLE
                            break
                        }
                        count = 0
                    }
                }
                
                if(count != 3){
                    resultTextview.setText("검색하신 정보에 대한 비밀번호가 없습니다.")
                    resultTextview.visibility = View.VISIBLE
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }




        })


    }

    fun show(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}