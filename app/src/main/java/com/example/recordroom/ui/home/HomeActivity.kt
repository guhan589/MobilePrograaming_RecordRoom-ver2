package com.example.recordroom.ui.home

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.recordroom.R
import com.example.recordroom.ui.commom.Permission
import com.example.recordroom.model.SharedUserData
import com.example.recordroom.ui.addroom.AddRoomActivity
import com.example.recordroom.ui.commom.RoomRecord
import com.example.recordroom.ui.login.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.appbar.view.*
import net.daum.mf.map.api.MapView


class HomeActivity : AppCompatActivity() {
    var roomdata = arrayListOf<RoomRecord>()
    var documentdata = arrayListOf<String>()

    var db : FirebaseFirestore? = null
    lateinit var mapView: MapView
    lateinit var mapViewContainer:ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar:Toolbar
        toolbar = appbar
        toolbar.appbar_textView.setText("방 구")
        toolbar.setTitle("")
        setSupportActionBar(toolbar)


        val progress = ProgressDialog(this)
        progress.setCancelable(false)
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.setMessage("데이터 읽어 오는 중...\n잠시만 기다려주세요.")

        val permission = Permission(this)
        permission.checkPermissions() //퍼미션 체크




        addBtn.setOnClickListener{
            val intent = Intent(this, AddRoomActivity::class.java);
            startActivity(intent)
        }







        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            progress.show()
            val selectItem = parent.getItemAtPosition(position) as RoomRecord
            val intent = Intent(this,DetailRoomActivity::class.java)

            intent.putExtra("roomName",selectItem.roomName) //방이름
            intent.putExtra("address",selectItem.address) // 방 주소
            intent.putExtra("latitude",selectItem.latitude) // 방 위도
            intent.putExtra("longitude",selectItem.longitude) // 방 경도
            intent.putExtra("imageUri",selectItem.imageUri) // 방 이미지 다운로드 URL
            intent.putExtra("imageName",selectItem.imageName) // 방 이미지 이름
            intent.putExtra("score1",selectItem.scores?.get(0)) // 방크기 점수
            intent.putExtra("score2",selectItem.scores?.get(1)) // 방 수압 점수
            intent.putExtra("score3",selectItem.scores?.get(2)) // 방 치안 점수
            intent.putExtra("score4",selectItem.scores?.get(3)) // 방 방음 점수
            intent.putExtra("score5",selectItem.scores?.get(4)) // 방 수압 점수
            intent.putExtra("score6",selectItem.scores?.get(5)) // 방 편의시설 점수
            intent.putExtra("documentdata",documentdata.get(position)) //document ID값
            startActivity(intent)
            progress.dismiss()
        }





    }

    fun downloagList(){
        /**
         * fireStore 내용가져오기
         * */
        roomdata.clear()
        documentdata.clear()
        db = FirebaseFirestore.getInstance()

        val userid = SharedUserData(this).getUser_id()
        val data = db!!.collection(userid!!).get()
            //data.result.size()==null
            .addOnSuccessListener { result  -> //컬렉션의 모든 문서 보기
            if(result != null){
                for (document in result ) {
                    val map = document.data
                    documentdata.add(document.id)
                    Log.d("TAG", "scores11: "+map.getValue("scores"))
                    roomdata.add(RoomRecord(
                        map.getValue("roomName") as String?,map.getValue("address") as String?,
                        map.getValue( "latitude") as Double?, map.getValue("longitude") as Double?,
                        map.getValue("imageUri") as ArrayList<String>?,
                        map.getValue("imageName") as ArrayList<String>?, map.getValue("scores") as ArrayList<Double>?

                    ))
                }


            }
            Log.d("downloagList", "data.size: "+roomdata.size)
            updateList()
        }.addOnFailureListener { exception ->
            Log.w("TAG", "Error getting documents: ", exception)
        }
    }
    fun updateList(){
        val adapter = ListAdapter(this, roomdata, documentdata,this)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    fun initMapview(){
        /**
         * 카카오 mapView 객체
         * */

        mapView = MapView(this)
        //mapViewContainer = map_view as ViewGroup
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
                startActivity(Intent(this,LoginActivity::class.java))

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


    override fun onStart() {
        super.onStart()
        downloagList()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}