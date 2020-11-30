package com.example.recordroom.UI

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.recordroom.R
import com.example.recordroom.function.GpsManager
import com.example.recordroom.function.Permission
import com.example.recordroom.model.SharedUserData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.bottomsheet_dialog.*
import kotlinx.android.synthetic.main.content_main.*
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class AddRoomActivity : AppCompatActivity(), MapView.POIItemEventListener , MapView.MapViewEventListener{
    
    /**
     * 방추가하기
     * **/
    //var data = arrayListOf<RoomRecord>()
    lateinit var mapView: MapView
    lateinit var mapViewContainer:ViewGroup
    var latitude:Double = 0.0
    var longitude:Double = 0.0
    val REQUEST_TEST = 1
    //private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)

        Permission(this).checkPermissions()// 퍼미션 체크
        
        //initMapview();//지도 생성

        val gpsManager = GpsManager(this) // GPS 모듈 통신 
        latitude = gpsManager.getLatitude()!!//경도
        longitude = gpsManager.getLongitude()!!//위도
        Log.d("onCreate", "latitude: " + latitude)
        Log.d("onCreate", "longitude: " + longitude)


        var user_id = SharedUserData(this).getUser_id();
        if (user_id.equals("") || user_id == null) {
            user_id = "없음"
        }
        Log.d("TAG", "user_id: " + user_id)

        reloctionBtn.setOnClickListener{ //새로고침 버튼(위치 값 변경)
            latitude = gpsManager.getLatitude()!!
            longitude = gpsManager.getLongitude()!!
            createOptionMapview(mapView)
        }


        /*bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object:
            BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> Toast.makeText(applicationContext, "STATE_COLLAPSED", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_EXPANDED -> Toast.makeText(applicationContext, "STATE_EXPANDED", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_DRAGGING -> Toast.makeText(applicationContext, "STATE_DRAGGING", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_SETTLING -> Toast.makeText(applicationContext, "STATE_SETTLING", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_HIDDEN -> Toast.makeText(applicationContext, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(applicationContext, "OTHER_STATE", Toast.LENGTH_SHORT).show()
                }
            }
        })*/

    }


    fun showBottomDialog(){
        /**
         * Bottom Sheet Dialog를 생성하고 이를 인터페이스에 띄움
         * 
         * */
        val bottomSheetDialog = RatingBottomDialogFragment().getInstance()
        bottomSheetDialog?.show(getSupportFragmentManager(),"bottomSheet")
    }

    fun initMapview(){
        /**
         * 카카오 mapView 객체 및 지도 이벤트 처리
         * */
        mapView = MapView(this)


        mapViewContainer = addRoomMapview as ViewGroup
        mapViewContainer.addView(mapView)
        mapView.setPOIItemEventListener(this)

    }

    fun createOptionMapview(mapView: MapView){
        /**
         * mapview 지도의 환경 설정(zoom level, 보여줄 좌표값)
         * */
        mapView.setZoomLevel(6,true)
        Log.d("createOptionMapview", "latitude1: "+latitude)
        Log.d("createOptionMapview", "longitude1: "+longitude)
        Log.d("createOptionMapview", "zoomLevel: "+mapView.zoomLevel)

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

    fun makeOverlay(mapView: MapView, latitude:Double, longitude : Double ){
        val mapCircle = MapCircle(MapPoint.mapPointWithCONGCoord(latitude,longitude),500, Color.argb(97,68,69,240),Color.argb(0,255,255,255))
        mapView.addCircle(mapCircle)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                val value = data?.getStringExtra("result")

                Log.d("TAG," ,"onActivityResult: "+value)
                //show("Result: " + data?.getStringExtra("result"))
            } else {   // RESULT_CANCEL
                //show("Failed")
            }

        }



    }


    override fun onResume() {
        initMapview()
        createOptionMapview(mapView)
        super.onResume()
    }
    override fun onPause() {

        mapViewContainer.removeView(mapView)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapViewContainer.removeView(mapView)
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {

    }
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        showBottomDialog() // bottomSheetDialog를 보여주는 메소드
      /*  if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED*/
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
        Log.d("zoomLevel", "onMapViewZoomLevelChanged: "+p1)
        Log.d("zoomLevel", "onMapViewZoomLevelChanged: "+p0?.zoomLevel)
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        /**
         * 사용자가 지도 위 한지점을 길게 누른 경우
         * */
    }
}