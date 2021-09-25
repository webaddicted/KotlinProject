package com.webaddicted.kotlinproject.view.fragment

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmGoogleMapBinding
import com.webaddicted.kotlinproject.global.caranim.CarAnimationHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.activity.MapActivity
import com.webaddicted.kotlinproject.view.base.BaseFragment

class CarAnimFrm : BaseFragment(R.layout.frm_google_map), OnMapReadyCallback,
    GoogleMap.OnMapClickListener {
    private var googleMap: GoogleMap? = null
    private lateinit var mBinding: FrmGoogleMapBinding
    private var marker: Marker? = null
    private var markerCount = 0
    private var mLastLocation: Location? = null
    private var oldLocation: Location? = null

    companion object {
        val TAG = CarAnimFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): CarAnimFrm {
            val fragment = CarAnimFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmGoogleMapBinding
        init()
        clickListener()

    }

    private fun init() {
//        Install mock location app then on developer option ,
//        in dev option click select mock location app then select mock app,
//        in mock app select to location by long press on map then start ride
        mBinding.toolbar.imgBack.visible()
        mBinding.relative.gone()
        mBinding.relative1.gone()
        mBinding.edtSearch.gone()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.google_map_title)
        initailizeMap()
        (activity as MapActivity).mapViewModel.locationUpdated.observe(
            this,
            { location ->
                mLastLocation = location
                addMarker(googleMap!!, location.latitude, location.longitude)
//                drawMarker(location!!)
            })
        (activity as MapActivity).getLocation(2, 3, 0)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
        }
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

        }
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            googleMap = map
        }
    }

    private fun addMarker(googleMap: GoogleMap, lat: Double, lon: Double) {
        try {
            if (markerCount == 1) {
                if (oldLocation != null) {
                    CarAnimationHelper(
                        googleMap,
                        1000,
                        object : CarAnimationHelper.UpdateLocationCallBack {
                            override fun onUpdatedLocation(updatedLocation: Location?) {
                                oldLocation = updatedLocation
                            }
                        }).animateMarker(mLastLocation!!, marker)
                } else {
                    oldLocation = mLastLocation
                }
            } else if (markerCount == 0) {
                if (marker != null) {
                    marker?.remove()
                }
                this.googleMap = googleMap
                val latLng = LatLng(lat, lon)
                marker = googleMap.addMarker(
                    MarkerOptions().position(LatLng(lat, lon))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                )
                googleMap.setPadding(2000, 4000, 2000, 4000)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                /*################### Set Marker Count to 1 after first marker is created ###################*/
                markerCount = 1
//                if (ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) !== PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) !== PackageManager.PERMISSION_GRANTED
//                ) { // TODO: Consider calling
//                    return
//                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

