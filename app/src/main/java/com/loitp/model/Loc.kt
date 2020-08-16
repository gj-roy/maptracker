package com.loitp.model

import com.google.android.gms.maps.model.LatLng
import com.loitp.util.LocUtil

data class Loc(
        var beforeTimestamp: Long = 0,
        var afterTimestamp: Long = 0,
        var beforeLatLng: LatLng? = null,
        var afterLatLng: LatLng? = null
) {
    //return in meter
    fun getDistanceM(): Float? {
        return LocUtil.getDistance(beforeLatLng = beforeLatLng, afterLatLng = afterLatLng)
    }

    fun getTimeInSecond(): Long {
        if (beforeTimestamp == 0L) {
            return 0
        }
        return afterTimestamp / 1000L - beforeTimestamp / 1000L
    }

    //return speed m/s
    fun getSpeedMs(): Float {
        val s = getDistanceM() ?: 0F
        val t = getTimeInSecond().toFloat()
        val value = s / t
        if (value.isNaN()) {
            return 0F
        } else {
            return value
        }
    }

}
