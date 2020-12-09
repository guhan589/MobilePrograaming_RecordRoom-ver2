package com.example.recordroom.ui.addroom

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Gallery
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast

import com.example.recordroom.R
import com.example.recordroom.model.RoomInform
import com.example.recordroom.model.SharedUserData
import com.example.recordroom.ui.commom.Permission
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.bottomsheet_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RatingBottomDialogFragment() : BottomSheetDialogFragment() {

    public var address:String = ""
    var db : FirebaseFirestore? = null
    var fbAuth : FirebaseAuth? = null
    var fbStorage : FirebaseStorage? = null
    var uriPhoto : Uri? = null

    var dataUri:Uri ?= null
    var imagegroup = arrayListOf<Bitmap>()
    val Gallery = 0
    lateinit var beforeActivity :Activity


    private var msgLo: LinearLayout? = null
    private var emailLo: LinearLayout? = null
    private var cloudLo: LinearLayout? = null
    private var bluetoothLo: LinearLayout? = null
    private var value1 = 0f //첫번째 score
    private var value2 = 0f //두번재 score
    private var value3 = 0f //세번째 score
    private var value4 = 0f// 네번째 score
    private var value5 = 0f //다섯번째 score
    private var value6 = 0f //여섯번째 score
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
        progress.setMessage("등록중입니다.")




        addImageBtn.setOnClickListener{
            Permission(context as Activity).checkPermissions()
            loadImage()
        }
        addRoomBtn.setOnClickListener{

            val scoreList = ArrayList<Float>()

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
                var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                var imgFileName = "IMAGE_" + timeStamp + "_.png"
                Log.d("TAG", "imgFileName: "+imgFileName)
                var storageRef = fbStorage?.reference?.child("images")?.child(imgFileName)

                storageRef?.putFile(dataUri!!)?.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val userInfo = RoomInform()

                        userInfo.rommName = roomTitle.text.toString()//제목
                        userInfo.address = address.replace("대한민국","") // 주소
                        userInfo.scores = scoreList //각 점수들 
                        userInfo.imageUrl = uri.toString() //이밎 다운로드 경로
                        userInfo.imagename = imgFileName
                        var userId = SharedUserData(activity).getUser_id()
                        if(userId == null)
                            userId = "null"
                        //db?.collection(userId)?.document(fbAuth?.uid.toString())?.set(userInfo)
                        db?.collection(userId)?.add(userInfo)
                        progress.dismiss()
                        dismiss()
                        beforeActivity.finish()
                    }
                }


               /* room["title"] = roomTitle.text.toString() //제목
                room["address"] = address.replace("대한민국","") // 주소
                room["scores"] = scoreList //각 점수
                var userId = SharedUserData(activity).getUser_id()
                if(userId == null)
                    userId = "null"
                db!!.collection(userId).add(room)
                    .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                        Log.d(
                            "TAG",
                            "onSuccess: " + documentReference.id
                        )
                        Toast.makeText(context,"추가 완료",Toast.LENGTH_SHORT).show()
                        dismiss() //다이얼로그 dismiss
                    })
                    .addOnFailureListener(OnFailureListener {
                        Toast.makeText(context,"통신 오류",Toast.LENGTH_SHORT).show()
                        dismiss() //다이얼로그 dismiss
                    })*/
            }


        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivityResult1: ")
        if(requestCode == Gallery){
            Log.d("TAG", "onActivityResult12: ")
            if(resultCode == RESULT_OK){
                //imgFileName = getImageNameToUri(data?.getData());

                Log.d("TAG", "onActivityResult   ${data.toString()} ")
                Log.d("TAG", "onActivityResult ${data?.dataString} ")

                //imgFileName = imgFileName.substring(imgFileName.lastIndexOf("/")+1)

                dataUri = data?.data
                var bitmap:Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,dataUri)
                imagegroup?.add(bitmap)
                Log.d("onActivityResult", "size: ${imagegroup?.size}")
                Log.d("onActivityResult4", "onActivityResult4: ")
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
                    R.id.score1 -> value1 = rating
                    R.id.score2 -> value2 = rating
                    R.id.score3 -> value3 = rating
                    R.id.score4 -> value4 = rating
                    R.id.score5 -> value5 = rating
                    R.id.score6 -> value6 = rating
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