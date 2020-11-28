package com.example.recordroom.UI

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import com.example.recordroom.R
import com.example.recordroom.function.GpsManager
import com.example.recordroom.function.MessageActivity
import com.example.recordroom.function.WriteRemarkActivity
import com.example.recordroom.model.SharedUserData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.activity_signup.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.util.HashMap

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

        val gpsManager = GpsManager(this) // GPS 모듈 통신 
        latitude = gpsManager.getLatitude()!!//경도
        longitude = gpsManager.getLongitude()!!//위도
        Log.d("TAG", "latitude: " + latitude)
        Log.d("TAG", "longitude: " + longitude)


        var user_id = SharedUserData(this).getUser_id();
        if (user_id.equals("") || user_id == null) {
            user_id = "없음"
        }
        Log.d("TAG", "user_id: " + user_id)

        btn.setOnClickListener {
            val dialog = RatingBottomDialogFragment().getInstance()
            //dialog.setContentView(R.layout.fragment_rating_bottom_dialog)
            /*dialog.setOnCancelListener {
            }*/

            //dialog.setCanceledOnTouchOutside(true)
            //dialog.create()
            dialog?.show(getSupportFragmentManager(),"bottomSheet")


            /*val sheetBehavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(bottomSheet)
            sheetBehavior.isFitToContents = true // the default
            sheetBehavior.peekHeight = 200*/
        }
        /*sremarkText.setOnClickListener{
            val intent = Intent(applicationContext, WriteRemarkActivity::class.java)
            startActivityForResult(intent, 1)
        }*/


        /*roomAddBtn.setOnClickListener{
            val db = FirebaseFirestore.getInstance()
            val user: MutableMap<String, Any> =
                    HashMap()
            user["first"] = "Ada"
            user["last"] = "Lovelace"
            user["born"] = 1815
            db.collection(user_id).add(user)
                    .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                        Log.d(
                                "TAG",
                                "onSuccess: " + documentReference.id
                        )
                    })
                    .addOnFailureListener(OnFailureListener {

                    })*/

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
        val mapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude)
        mapView.setMapCenterPoint(mapPoint,true)
        val marker = MapPOIItem()
        marker.mapPoint = mapPoint
        marker.tag=1
        marker.itemName = "내위치"
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