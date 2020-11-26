package com.example.recordroom.UI

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.recordroom.R
import com.example.recordroom.function.Permission
import com.example.recordroom.model.SharedUserData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.appbar.view.*
import net.daum.mf.map.api.MapView
import java.util.*


class MainActivity : AppCompatActivity() {
    var data = arrayListOf<RoomRecord>()
    lateinit var mapView: MapView
    lateinit var mapViewContainer:ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val name = intent.getStringExtra("name")
        Log.d("TAG", "name: "+name)
        val toolbar:Toolbar
        toolbar = appbar
        toolbar.appbar_textView.setText("방 구")
        toolbar.setTitle("")
        setSupportActionBar(toolbar)


        val permission = Permission(this)
        permission.checkPermissions()

        data.add(RoomRecord("0", "0", "0", "d"))
        data.add(RoomRecord("1", "0", "0", "d"))


       // initMapview();// mapview생성


        val db = FirebaseFirestore.getInstance()
        val adapter = ListAdapter(this, data)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()


        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as String
            Log.d("TAG", "selectItem: "+selectItem)
        }


        val user: MutableMap<String, Any> =
            HashMap()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815
        conBtn.setOnClickListener{
            db.collection("user1").add(user)
                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                    Log.d(
                        "TAG",
                        "onSuccess: " + documentReference.id
                    )
                })
                .addOnFailureListener(OnFailureListener { })
        }

    }

    fun initMapview(){
        /**
         * 카카오 mapView 객체
         * */


        mapView = MapView(this)
        mapViewContainer = map_view as ViewGroup
        mapViewContainer.addView(mapView)
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //menu xml 설정
        menuInflater.inflate(R.menu.appbarmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//menu 이벤트 처리

        when(item.itemId){
            R.id.logout_Btn ->{//로그아웃 버튼 누를시
                logout_dialog()
                /*SharedUserData(this).reset()
                finish()//엑티비티 종료*/
            }
        }
        return super.onOptionsItemSelected(item)
    }


}