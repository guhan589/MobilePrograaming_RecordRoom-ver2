package com.example.recordroom.ui.addroom

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast

import com.example.recordroom.R
import com.example.recordroom.model.SharedUserData
import com.example.recordroom.ui.commom.Permission
import com.example.recordroom.ui.commom.RoomRecord
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.bottomsheet_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RatingBottomDialogFragment() : BottomSheetDialogFragment() {

    public var address:String = ""
    var latitude : Double = 0.0
    var longitude : Double = 0.0


    var db : FirebaseFirestore? = null
    var fbAuth : FirebaseAuth? = null
    var fbStorage : FirebaseStorage? = null
    var send_uriGroup : ArrayList<String>? = arrayListOf<String>()
    var user_uriGroup : ArrayList<Uri>? = arrayListOf<Uri>()
    var imageNameGrop : ArrayList<String> = arrayListOf<String>()
    var dataUri:Uri ?= null
    var imagegroup = arrayListOf<Bitmap>()
    val Gallery = 0
    lateinit var beforeActivity :Activity

    private var value1 = 0.0 //첫번째 score
    private var value2 = 0.0 //두번재 score
    private var value3 = 0.0 //세번째 score
    private var value4 = 0.0// 네번째 score
    private var value5 = 0.0 //다섯번째 score
    private var value6 = 0.0 //여섯번째 score
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.bottomsheet_dialog, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        beforeActivity = AddRoomActivity.getInstance()

        ratingChanged() //raingBar 체인지 리스너 실행
        updateList()

        val progress = ProgressDialog(context)
        progress.setCancelable(false)
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.setMessage("등록중입니다.\n(최대 1분 소요)")




        addImageBtn.setOnClickListener{
            Permission(context as Activity).checkPermissions()
            if(imagegroup.size>=3){
                Toast.makeText(context,"이미지는 최대 3장까지 업로드 가능합니다.",Toast.LENGTH_SHORT).show()
            }else
                loadImage()
        }
        addRoomBtn.setOnClickListener{

            val scoreList = ArrayList<Double>()

            progress.show()
            /**
             * 스코어
            * */
            scoreList.add(value1)
            scoreList.add(value2)
            scoreList.add(value3)
            scoreList.add(value4)
            scoreList.add(value5)
            scoreList.add(value6)


            db = FirebaseFirestore.getInstance()
            fbAuth = FirebaseAuth.getInstance()
            fbStorage = FirebaseStorage.getInstance()

            val room: MutableMap<String, Any> = HashMap()
            if(roomTitle.text!!.toString().equals("")){
                Toast.makeText(context,"제목을 입력하세요.",Toast.LENGTH_SHORT).show()
            }else{
                progress.show()
                var i =0;
                var userId = SharedUserData(activity).getUser_id()
                if(userId == null)
                    userId = "null"
                var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                for(i in 0..user_uriGroup!!.size-1){
                    var imgFileName = "IMAGE_" + timeStamp + "_$i" + "_.png"
                    var storageRef = fbStorage?.reference?.child(userId!!)?.child(imgFileName)

                    storageRef?.putFile(user_uriGroup!!.get(i))?.addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->

                            send_uriGroup?.add(uri.toString())
                            imageNameGrop?.add(imgFileName) //이미지 파일 이름 list에 추가
                        }
                    }

                }


             



                Handler().postDelayed({
                    val userInfo = RoomRecord()
                    Log.d("TAG", "send_uriGroup.size: ${send_uriGroup?.size} ")
                    Log.d("TAG", "imageNameGrop.size: ${imageNameGrop.size} ")
                    userInfo.roomName = roomTitle.text.toString()//제목
                    userInfo.address = address.replace("대한민국","") // 주소
                    userInfo.latitude = latitude
                    userInfo.longitude = longitude
                    userInfo.scores = scoreList //각 점수들
                    userInfo.imageUri = send_uriGroup //이미지 다운로드 경로
                    userInfo.imageName = imageNameGrop // 이미지 이름


                    //db?.collection(userId)?.document(fbAuth?.uid.toString())?.set(userInfo)
                    db?.collection(userId!!)?.add(userInfo) //Firestore 에 방 정보기입
                    progress.dismiss()
                    dismiss()
                    beforeActivity.finish()
                },6000)

            }


        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Gallery){

            if(resultCode == RESULT_OK){
                //imgFileName = getImageNameToUri(data?.getData());


                //imgFileName = imgFileName.substring(imgFileName.lastIndexOf("/")+1)

                dataUri = data?.data
                user_uriGroup?.add(dataUri!!) //사용자가 추가한 이미지 uri 배열에 담김
                var bitmap:Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,dataUri)
                imagegroup?.add(bitmap)

                imagelist.visibility=View.VISIBLE
                updateList();

            }
        }
    }
    fun getInstance(): RatingBottomDialogFragment? {
        return RatingBottomDialogFragment()
    }
    fun updateList(){ //사용자가 선택한 이미지를 리스트뷰로 보여줌
        if(imagegroup.size == 0){

        }else{
            Log.d("updateList", "size: "+imagegroup?.size)
            val adapter = imageAdapter(imagegroup!!)
            imagelist.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }
    fun ratingChanged(){
        var ratingBar = listOf<RatingBar>(score1,score2,score3,score4,score5,score6)
        ratingBar.forEach{
            it.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                var id = ratingBar.id
                when(id){
                    R.id.score1 -> {
                        value1 = rating.toDouble()
                        Log.d("ratingChanged", "value1: $value1")
                    }
                    R.id.score2 ->{
                        value2 = rating.toDouble()
                        Log.d("ratingChanged", "value2: $value2")
                    }
                    R.id.score3 ->{
                        value3 = rating.toDouble()
                        Log.d("ratingChanged", "value3: $value3")
                    }
                    R.id.score4 ->{
                        value4 = rating.toDouble()
                        Log.d("ratingChanged", "value4: $value4")
                    }
                    R.id.score5 ->{
                        value5 = rating.toDouble()
                        Log.d("ratingChanged", "value5: $value5")
                    }
                    R.id.score6 ->{
                        value6 = rating.toDouble()
                        Log.d("ratingChanged", "value6: $value6")
                    }

                }
                Log.d("TAG", "$ratingBar:  $rating")
            }
        }
    }

    fun loadImage(){ //디바이스 이미지 불러오기
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Load Picture"),Gallery)
    }

}