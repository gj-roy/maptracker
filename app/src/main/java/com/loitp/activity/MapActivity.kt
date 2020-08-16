package com.loitp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.core.base.BaseFontActivity
import com.core.utilities.LDialogUtil
import com.core.utilities.LMathUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.interfaces.Callback2
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.loitp.R
import com.loitp.model.Loc
import com.loitp.util.LocUtil
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : BaseFontActivity(), OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 8000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
        private const val ZOOM_TO = 11f
    }

    private var googleApiClient: GoogleApiClient? = null
    private var firstLocationMarker: Marker? = null
    private var currentLocationMarker: Marker? = null
    private var mGoogleMap: GoogleMap? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null
    private val listLoc = ArrayList<Loc>()
    private var isShowDialogCheck = false

    override fun setTag(): String? {
        return "loitpp" + javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_map
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initLocation()

        btPause.setSafeOnClickListener {
//TODO
        }
        btContinue.setSafeOnClickListener {
//TODO
        }
        btStop.setSafeOnClickListener {
//TODO
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isShowDialogCheck) {
            checkPermission()
        }
    }

    //region permisson
    private fun checkPermission() {
        isShowDialogCheck = true
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            buildClient()
                        } else {
                            showShouldAcceptPermission()
                        }

                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                        isShowDialogCheck = true
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }

    private fun showSettingsDialog() {
        val alertDialog = LDialogUtil.showDialog2(
                context = activity,
                title = getString(R.string.need_permisson),
                msg = "This app needs permission to use this feature. You can grant them in app settings.",
                button1 = getString(R.string.setting),
                button2 = getString(R.string.cancel),
                callback2 = object : Callback2 {
                    override fun onClick1() {
                        isShowDialogCheck = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, 101)
                    }

                    override fun onClick2() {
                        onBackPressed()
                    }
                })
        alertDialog.setCancelable(false)
    }

    private fun showShouldAcceptPermission() {
        val alertDialog = LDialogUtil.showDialog2(
                context = activity,
                title = getString(R.string.need_permisson),
                msg = getString(R.string.need_permisson_to_use_app),
                button1 = getString(R.string.ok),
                button2 = getString(R.string.cancel),
                callback2 = object : Callback2 {
                    override fun onClick1() {
                        checkPermission()
                    }

                    override fun onClick2() {
                        onBackPressed()
                    }
                })
        alertDialog.setCancelable(false)
    }
    //endregion

    private fun onChangeLocation() {
//        logD("onChangeLocation " + mCurrentLocation?.latitude + " - " + mCurrentLocation?.longitude)

        currentLocationMarker?.remove()
        mCurrentLocation?.let { location ->

            val latRound = LMathUtil.roundDouble(value = location.latitude, newScale = 4)
            val lngRound = LMathUtil.roundDouble(value = location.longitude, newScale = 4)

//            val latRound = location.latitude
//            val lngRound = location.longitude

            val latLng = LatLng(latRound, lngRound)

            val beforeLoc = listLoc.lastOrNull()
            val beforeTimestamp = beforeLoc?.afterTimestamp ?: 0
            val beforeLatLng = beforeLoc?.afterLatLng
            val afterLatLng = latLng
            val loc = Loc(
                    beforeTimestamp = beforeTimestamp,
                    afterTimestamp = System.currentTimeMillis(),
                    beforeLatLng = beforeLatLng,
                    afterLatLng = afterLatLng
            )

            val log = "${loc.beforeTimestamp} : ${loc.beforeLatLng?.latitude} - ${loc.beforeLatLng?.longitude} ~ ${loc.afterLatLng?.latitude} - ${loc.afterLatLng?.longitude} -> ${loc.getDistance()}(m) - ${loc.getTimeInSecond()}(s) -> ${loc.getSpeed()}(m/s)"
            logD("onChangeLocation $log")

            listLoc.add(element = loc)

            if (firstLocationMarker == null) {
                addFirstLocationMaker(latLng = latLng, location = location)
            } else {
                updateCurrentLocationMaker(latLng = latLng, location = location)
                //draw router
                drawPolyLineOnMap()
            }
        }
    }

    private fun addFirstLocationMaker(latLng: LatLng, location: Location) {
//        logD("addFirstLocationMaker")
        val markerOptions = LocUtil.getMaker(context = activity, latLng = latLng, location = location, color = BitmapDescriptorFactory.HUE_RED)
        firstLocationMarker = mGoogleMap?.addMarker(markerOptions)
        mGoogleMap?.let {
            it.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            it.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_TO))
        }
    }

    private fun updateCurrentLocationMaker(latLng: LatLng, location: Location) {
//        logD("updateCurrentLocationMaker")
        val markerOptions = LocUtil.getMaker(context = activity, latLng = latLng, location = location, color = BitmapDescriptorFactory.HUE_CYAN)
        currentLocationMarker = mGoogleMap?.addMarker(markerOptions)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//        mGoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_TO))
    }

    private fun initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                onChangeLocation()
            }
        }
        mLocationRequest = LocationRequest()
        mLocationRequest?.let {
            it.interval = UPDATE_INTERVAL_IN_MILLISECONDS
            it.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(it)
            mLocationSettingsRequest = builder.build()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap?.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isZoomGesturesEnabled = true
            uiSettings.isCompassEnabled = true
//            uiSettings.isMapToolbarEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isRotateGesturesEnabled = true
            uiSettings.isScrollGesturesEnabled = true
            uiSettings.isTiltGesturesEnabled = true
        }
        buildClient()
    }

    private fun drawPolyLineOnMap() {
        val listLatLng = ArrayList<LatLng>()
        listLoc.forEach { loc ->
            loc.afterLatLng?.let {
                listLatLng.add(element = it)
            }
        }

        if (listLoc.isNotEmpty()) {
            val polyOptions = PolylineOptions()
            polyOptions.color(Color.RED)
            polyOptions.width(15f)
            polyOptions.addAll(listLatLng)
            mGoogleMap?.let {
                it.clear()
                it.addPolyline(polyOptions)
            }
            val builder = LatLngBounds.Builder()
            for (latLng in listLatLng) {
                builder.include(latLng)
            }
//            val bounds = builder.build()
//            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
//            mGoogleMap?.animateCamera(cameraUpdate)
        }
    }

    private fun buildClient() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (googleApiClient == null) {
                    buildGoogleApiClient()
                }
                mGoogleMap?.isMyLocationEnabled = true
            }
        } else {
            if (googleApiClient == null) {
                buildGoogleApiClient()
            }
            mGoogleMap?.isMyLocationEnabled = true
        }
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        logD("onConnected")
        startLocationUpdates()
        btPause.visibility = View.VISIBLE
    }

    override fun onConnectionSuspended(i: Int) {
        logD("onConnectionSuspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        logD("onConnectionFailed")
    }

    private fun startLocationUpdates() {
        mSettingsClient?.let { settingsClient ->
            settingsClient.checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                            onChangeLocation()
                        } else {
                            showShort("Dont have permission ACCESS_FINE_LOCATION && ACCESS_COARSE_LOCATION")
                        }
                    }
                    .addOnFailureListener(this) { e ->
                        showShort(e.toString())
                        onChangeLocation()
                    }
        }
    }
}
