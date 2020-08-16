package com.loitp.util

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class LocUtil {
    companion object {
        //BitmapDescriptorFactory.HUE_RED
        fun getMaker(context: Context, latLng: LatLng, location: Location, color: Float): MarkerOptions {
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color))
            markerOptions.position(latLng)

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providerList = locationManager.allProviders

            if (providerList.size > 0) {
                val longitude = location.longitude
                val latitude = location.latitude
                val geoCoder = Geocoder(context.applicationContext, Locale.getDefault())
                try {
                    val listAddresses = geoCoder.getFromLocation(latitude, longitude, 1)
//                    logD("listAddresses " + LApplication.gson.toJson(listAddresses))
                    if (listAddresses.isNullOrEmpty()) {
                        //do nothing
                    } else {
                        markerOptions.title(listAddresses[0].getAddressLine(0))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return markerOptions
        }
    }
}