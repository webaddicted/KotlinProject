package com.webaddicted.kotlinproject.view.fragment

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmGoogleMapBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.constant.AppConstant
import com.webaddicted.kotlinproject.view.activity.MapActivity
import com.webaddicted.kotlinproject.view.base.BaseFragment

class GoogleMapFrm : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private var fancyMarker: Marker? = null
    private var fencyLocation: Location? = null
    private lateinit var mLatLongList: ArrayList<LatLng>
    private var userMarker: Marker? = null
    private var googleMap: GoogleMap? = null
    private lateinit var mBinding: FrmGoogleMapBinding
    // Draw Geofence circle on GoogleMap
    private var geoFenceCircle: Circle? = null

//    private val mapViewModel: MapViewModel by inject()
//    private lateinit var userCurrentLocation: Location

    companion object {
        val TAG = GoogleMapFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): GoogleMapFrm {
            val fragment = GoogleMapFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_google_map
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmGoogleMapBinding
        init()
        clickListener()

    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.google_map_title)
        mLatLongList = ArrayList()
        initailizeMap()
        (activity as MapActivity).mapViewModel.locationUpdated.observe(
            this,
            Observer { location ->
                fencyLocation = location
                drawMarker(location!!)
            })

    }

    private fun clickListener() {
        mBinding.btnGoogleMap.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnCreatePolyline.setOnClickListener(this)
        mBinding.edtSearch.setOnClickListener(this)
        mBinding.btnGeoFencingStart.setOnClickListener(this)
        mBinding.btnGeoFencingStop.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_google_map -> {
                (activity as MapActivity).getLocation(2, 3, 0)
            }
            R.id.btn_create_polyline -> {
            }
            R.id.edt_search -> GlobalUtility.showToast("login hit")
            R.id.btn_geo_fencing_start -> startGeoFancy()
            R.id.btn_geo_fencing_stop -> stopGeoFancy()
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun stopGeoFancy() {
        GlobalUtility.showOfflineNotification(activity!!, "stop geo fency", "test work flow")
        (activity as MapActivity).stopGeoFencing(fancyMarker, geoFenceCircle)
    }

    private fun startGeoFancy() {
        if (fencyLocation != null) {
            GlobalUtility.showOfflineNotification(activity!!, "start geo fency", "let move")
            val latLng = LatLng(fencyLocation!!.latitude, fencyLocation!!.longitude)
            fancyMarker?.remove()
            fancyMarker = googleMap?.addMarker(
                MarkerOptions().position(latLng).title("This is Geo fency location").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            (activity as MapActivity).startGeoFencing(fencyLocation!!, fancyMarker)
            drawGeofence(fancyMarker)
        }
    }


    private fun drawGeofence(fancyMarker: Marker?) {
        Log.d(TAG, "drawGeofence()")

        if (geoFenceCircle != null)
            geoFenceCircle!!.remove()
        val circleOptions = CircleOptions()
            .center(fancyMarker?.position)
            .radius(AppConstant.GEOFENCE_RADIUS.toDouble())
            .fillColor(ContextCompat.getColor(context!!,R.color.geo_fency_color))
            .strokeColor(ContextCompat.getColor(context!!,R.color.geo_fency_color))
            .strokeWidth(1f)
        geoFenceCircle = googleMap?.addCircle(circleOptions)
    }

    private fun initailizeMap() {
        if (googleMap == null) {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onMapClick(latLng: LatLng?) {
        if (latLng != null) {
             googleMap?.addMarker(
                MarkerOptions().position(latLng).title("This is Me").icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_YELLOW
                    )
                )
            )
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

            mLatLongList.add(latLng)
//            if (mLatLongList?.size == 2) drawPolyLine()
            if (mLatLongList.size == 3) mLatLongList.clear()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            googleMap = map
        }
    }

    private fun drawMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (userMarker == null) googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                15f
            )
        )
        userMarker?.remove()
        userMarker = googleMap?.addMarker(
            MarkerOptions().position(latLng).title("This is Me").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
        )

    }
//    private fun drawPolyLine() {
//        val url = getDirectionsUrl(mLatLongList?.get(0), mLatLongList?.get(1))
//        val downloadTask = DownloadTask()
//        // Start downloading json data from Google Directions API
//        downloadTask.execute(url)
//    }
}

