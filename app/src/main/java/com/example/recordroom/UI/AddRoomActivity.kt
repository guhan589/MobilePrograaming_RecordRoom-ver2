package com.example.recordroom.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.example.recordroom.R
import com.example.recordroom.function.GpsManager
import com.example.recordroom.function.MessageActivity
import com.example.recordroom.function.WriteRemarkActivity
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.activity_signup.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class AddRoomActivity : AppCompatActivity(), MapView.POIItemEventListener {
    //var data = arrayListOf<RoomRecord>()
    lateinit var mapView: MapView
    lateinit var mapViewContainer:ViewGroup
    var latitude:Double = 0.0
    var longitude:Double = 0.0
    val REQUEST_TEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)


        initMapview();//지도 생성

        val gpsManager = GpsManager(this)
        latitude = gpsManager.getLatitude()!!
        longitude = gpsManager.getLongitude()!!
        Log.d("TAG", "latitude: "+latitude)
        Log.d("TAG", "longitude: "+longitude)

        /*sremarkText.setOnClickListener{
            val intent = Intent(applicationContext, WriteRemarkActivity::class.java)
            startActivityForResult(intent, 1)
        }*/

    }

    fun initMapview(){
        /**
         * 카카오 mapView 객체
         * */

        mapView = MapView(this)
        mapViewContainer = addRoomMapview as ViewGroup
        mapViewContainer.addView(mapView)
        mapView.setPOIItemEventListener(this)

    }

    fun createOptionMapview(mapView: MapView){
        /**
         * mapview 지도 이벤트 처리
         * */
        mapView.setZoomLevel(5,true)
        Log.d("TAG", "latitude1: "+latitude)
        Log.d("TAG", "longitude1: "+longitude)
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude),true)
        val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        val marker = MapPOIItem()
        marker.mapPoint = mapPoint
        marker.tag=1
        marker.markerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker)
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

    override fun onStart() {
        createOptionMapview(mapView) //지도 이벤트 설정
        super.onStart()
    }
    override fun onPause() {

        mapViewContainer.removeView(mapView)
        super.onPause()
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {

    }
}