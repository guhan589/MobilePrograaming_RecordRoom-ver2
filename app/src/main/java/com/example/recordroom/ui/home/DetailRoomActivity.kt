package com.example.recordroom.ui.home

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.example.recordroom.R
import com.example.recordroom.model.SharedUserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail_room.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.net.URL

class DetailRoomActivity : AppCompatActivity(), MapView.POIItemEventListener , MapView.MapViewEventListener {
    var db : FirebaseFirestore? = null
    var fbStorage : FirebaseStorage? = null
    lateinit var mapView: MapView
    lateinit var mapViewContainer:ViewGroup
    var latitude:Double = 0.0
    var longitude:Double = 0.0
    var imageGroup = listOf<Int>(R.id.imageView1,R.id.imageView2,R.id.imageView3)
    var ratingGroup = listOf<Int>(R.id.ratingscore1,R.id.ratingscore2,R.id.ratingscore3,R.id.ratingscore4,R.id.ratingscore5,R.id.ratingscore6)
    var count = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_room)

        val progress = ProgressDialog(this)
        progress.setCancelable(false)
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.setMessage("삭제 중...\n잠시만 기다려주세요.")

        /**
         * 사용자가 클릭한 방정보 가져오기
         * */
        var roomName = intent.getStringExtra("roomName") //방이름
        var address = intent.getStringExtra("address") //방주소
        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude",0.0)
        var imageUri = intent.getStringArrayListExtra("imageUri") //이미지 주소
        var imageName = intent.getStringArrayListExtra("imageName") // 이미지 이름
        var score1 = intent.getDoubleExtra("score1",0.0) //방크기 점수
        var score2 = intent.getDoubleExtra("score2",0.0) //수압 점수
        var score3 = intent.getDoubleExtra("score3",0.0) //치안 점수
        var score4 = intent.getDoubleExtra("score4",0.0) //방음 점수
        var score5 = intent.getDoubleExtra("score5",0.0) //수압 점수
        var score6 = intent.getDoubleExtra("score6",0.0) //편의시설 점수
        var documentdata = intent.getStringExtra("documentdata") //문서이름
        
        val scores = ArrayList<Double>()
        scores.add(score1) // 방크기 점수 
        scores.add(score2) // 수압 점수
        scores.add(score3) // 치안 점수
        scores.add(score4) // 방음 점수
        scores.add(score5) // 수압 점수
        scores.add(score6) // 편의시설 점수

        var status = true
        imageBtn.setOnClickListener{//이미지 보기 버튼 클릭시
            if(status){//활성화
                imagegroup.visibility = View.VISIBLE // 이미지 구역을 보이게 활성화
                status = false
            }else{//비활성화
                imagegroup.visibility = View.GONE //이미지 구역을 안보이게 활성화
                status = true
            }
            
        }
       // initMapview();


        roomtittleTextview.setText(roomName) //방 이름 설정
        roomaddressTextview.setText(address) // 방주소 설정
        for(i in 0..scores.size-1){ // 각 score값을 설정
            setRating(ratingGroup.get(i),scores.get(i))
        }
        for( i in 0..imageUri!!.size -1){ //이미지 url를 이용하여 이미지 다운로드
            var image_task: URLtoBitmapTask = URLtoBitmapTask()
            image_task = URLtoBitmapTask().apply {
                url = URL(imageUri.get(i))
            }
            var bitmap: Bitmap = image_task.execute().get()
            val imageView = imageGroup.get(i)
            setImage(imageView,bitmap)
        }

        /**
         * FireStore에 이미지 제거
         * **/
        val userId = SharedUserData(this).getUser_id() //사용자 id값 넘김
        deleteBtn.setOnClickListener { // 삭제하기 버튼
            progress.show()
            db = FirebaseFirestore.getInstance()
            fbStorage = FirebaseStorage.getInstance()
            for(i in 0..imageName!!.size-1) {
                val path = "$userId/${imageName.get(i)}"
                Log.d("TAG", "path: $path")
                val deleteFile = fbStorage!!.reference.child(path)
                deleteFile.delete().addOnSuccessListener {
                    count++

                    if(count == imageName.size){
                        progress.dismiss()
                        finish()
                    }

                }
            }
            db!!.collection(userId!!).document(documentdata!!).delete().addOnSuccessListener {
                Toast.makeText(this,"계정 삭제 완료",Toast.LENGTH_SHORT).show()
            }

        }


    }

    fun setRating(id:Int, score:Double){ //rating값 설정
        Log.d("TAG", "setRating: $id")
        Log.d("TAG", "setRating: $score")
        when(id){
            R.id.ratingscore1 -> ratingscore1.rating = score.toFloat()
            R.id.ratingscore2 -> ratingscore2.rating = score.toFloat()
            R.id.ratingscore3 -> ratingscore3.rating = score.toFloat()
            R.id.ratingscore4 -> ratingscore4.rating = score.toFloat()
            R.id.ratingscore5 -> ratingscore5.rating = score.toFloat()
            R.id.ratingscore6 -> ratingscore6.rating = score.toFloat()

        }
    }
    fun setImage(id:Int, bitmap:Bitmap){
        /**
         * 이미지 설정
         * */

        when(id){
            R.id.imageView1 ->{
                imageView1.visibility = View.VISIBLE
                imageView1.setImageBitmap(bitmap)
            }
            R.id.imageView2 -> {
                imageView2.visibility = View.VISIBLE
                imageView2.setImageBitmap(bitmap)
            }
            R.id.imageView3 -> {
                imageView3.visibility = View.VISIBLE
                imageView3.setImageBitmap(bitmap)
            }

        }
    }
    fun initMapview(){
        /**
         * 카카오 mapView 객체 및 지도 이벤트 처리
         * */
        mapView = MapView(this)
        mapViewContainer = roomMap as ViewGroup
        mapViewContainer.addView(mapView)
        mapView.setPOIItemEventListener(this)
        createOptionMapview(mapView)
    }

    fun createOptionMapview(mapView: MapView){
        /**
         * mapview 지도의 환경 설정(zoom level, 보여줄 좌표값)
         * */
        mapView.setZoomLevel(1,true)
        Log.d("createOptionMapview", "latitude1: "+latitude)
        Log.d("createOptionMapview", "longitude1: "+longitude)
        //Log.d("createOptionMapview", "zoomLevel: "+mapView.zoomLevel)

        val mapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude)
        mapView.setMapCenterPoint(mapPoint,true)
        val marker = MapPOIItem()
        marker.mapPoint = mapPoint
        marker.tag=1
        marker.itemName = "현재위치"
        marker.markerType = MapPOIItem.MarkerType.RedPin

        mapView.removeAllPOIItems() // 지도 위에 몬든 마커 지우기
        mapView.addPOIItem(marker)
    }
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {

    }
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
        /**
         * 사용자가 지도 위한 지점을 더블 터치한 경우
         * */

    }

    override fun onMapViewInitialized(p0: MapView?) {
        /**
         * Mapview가 사용가능한 상태가 되어있음을 알려주는 메소드
         *
         * **/
        Log.d("onMapViewInitialized", "onMapViewInitialized: ")

    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        /**
         * 사용자가 지도 드래그를 시작한 경우
         *
         * */
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        /**
         * 지도의 이동이 완료된 경우
         * **/
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        /**
         * 지도 중심 좌표가 이동한 경우 호출
         * **/
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        /**
         *
         * 사용자가 지도 드래그를 끝낸 경우
         * **/
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        /***
         * 사용자가 지도 위를 터치한 경우
         * */
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        /**
         *지도 확대.축소 레벨이 변경된 경우
         * */

    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        /**
         * 사용자가 지도 위 한지점을 길게 누른 경우
         * */
    }

    override fun onResume() {
        initMapview()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewContainer.removeView(mapView)
    }
    class URLtoBitmapTask() : AsyncTask<Void, Void, Bitmap>() {
        //액티비티에서 설정해줌
        lateinit var url: URL
        override fun doInBackground(vararg params: Void?): Bitmap {
            val bitmap = BitmapFactory.decodeStream(url.openStream())
            return bitmap
        }
        override fun onPreExecute() {
            super.onPreExecute()

        }
        override fun onPostExecute(result: Bitmap) {
            super.onPostExecute(result)
        }
    }
}

