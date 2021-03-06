package com.example.recordroom.ui.commom

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import java.util.*
import kotlin.String as String

public class GpsManager(val context: Context) {

    var location: Location? = null
    var address = ""
    private val geocoder = Geocoder(context, Locale.getDefault())

    init {
        initGpsCheck()
    }

    @SuppressLint("MissingPermission")
    fun initGpsCheck() {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object: LocationListener {
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onLocationChanged(location: Location) {
                this@GpsManager.location = location
                //makeAddress()
            }
        }

        val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if(networkLocation!=null) {
            location = networkLocation
            //makeAddress()
        }

        val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(gpsLocation!=null) {
            location = gpsLocation
            //makeAddress()
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, locationListener)

        makeAddress()// 주소 생성
    }

    /**
     * 주소값 가져오기
     */
    private fun makeAddress() {
        try {
            address = geocoder.getFromLocation(
                location?.latitude?: (-1).toDouble(),
                location?.longitude?: (-1).toDouble(),
                1
            )[0].getAddressLine(0)

            Log.d("makeAddress", "address: "+address)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getLatitude(): Double? {
        return location?.latitude
    }
    fun getLongitude(): Double? {
        return location?.longitude
    }
    fun getAddr():String{
        return address
    }
}