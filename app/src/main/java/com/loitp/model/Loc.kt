package com.loitp.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.loitp.util.LocUtil

data class Loc(
        var beforeTimestamp: Long = 0,
        var afterTimestamp: Long = 0,
        var beforeLatLng: LatLng? = null,
        var afterLatLng: LatLng? = null
) {
    //return in meter
    fun getDistance(): Float? {
        return LocUtil.getDistance(beforeLatLng = beforeLatLng, afterLatLng = afterLatLng)
    }

    fun getTimeInSecond(): Long {
        if (beforeTimestamp == 0L) {
            return 0
        }
        return afterTimestamp / 1000L - beforeTimestamp / 1000L
    }

    //return speed m/s
    fun getSpeed(): Float {
        val s = getDistance() ?: 0F
        val t = getTimeInSecond().toFloat()
        return s / t
    }
}
