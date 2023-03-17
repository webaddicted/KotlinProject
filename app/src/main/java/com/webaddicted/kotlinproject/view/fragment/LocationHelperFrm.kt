package com.webaddicted.kotlinproject.view.fragment

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmLocationHelperBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.common.LocationBean
import com.webaddicted.kotlinproject.view.adapter.LocationAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.base.LocationHelper

class LocationHelperFrm : BaseFragment(R.layout.frm_location_helper) {
    private lateinit var adapter: LocationAdapter
    private lateinit var mBinding: FrmLocationHelperBinding
    private var locationBean: ArrayList<LocationBean>? = null

    companion object {
        val TAG = LocationHelperFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): LocationHelperFrm {
            val fragment = LocationHelperFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmLocationHelperBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.location_helper)
        locationBean = ArrayList()
        setAdapter()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnFetchLocation.setOnClickListener(this)
        mBinding.btnContinueLocation.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> backDispatcher.onBackPressed()
            R.id.btn_fetch_location -> fetchLocation()
            R.id.btn_continue_location -> fetchUpdatedLocation()
        }
    }



    private fun setAdapter() {
        adapter = LocationAdapter(locationBean)
        mBinding.includeRv.rvApps.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.includeRv.rvApps.adapter = adapter

    }


    private fun fetchLocation() {
        LocationHelper.getLocation(
            mActivity,
            object : LocationHelper.Companion.LocationChangeListener {
                override fun onUpdatedLocation(location: Location, address: String) {
                    GlobalUtility.print(TAG, "updatedLat: ${location.latitude} long: ${location.longitude}")
                    locationBean?.add(0, LocationBean().apply {
                        lat = location.latitude.toString()
                        lon = location.longitude.toString()
                        addre = address
                    })
                    mBinding.includeRv.imgNoDataFound.gone()
                    adapter.notifyAdapter(locationBean!!)
                }

                override fun onError(string: String) {
                    GlobalUtility.showToast(string)
                    GlobalUtility.print(TAG, "error: $string")
                }
            })
        LocationHelper.isAddressEnabled(true)
    }
    private fun fetchUpdatedLocation() {
        LocationHelper.getLocation(
            mActivity,
            3,3,5,
            object : LocationHelper.Companion.LocationChangeListener {
                override fun onUpdatedLocation(location: Location, address: String) {
                    GlobalUtility.print(TAG, "updatedLat: ${location.latitude} long: ${location.longitude}")
                    locationBean?.add(0, LocationBean().apply {
                        lat = location.latitude.toString()
                        lon = location.longitude.toString()
                        addre = address
                    })
                    mBinding.includeRv.imgNoDataFound.gone()
                    adapter.notifyAdapter(locationBean!!)
                }

                override fun onError(string: String) {
                    GlobalUtility.showToast(string)
                    GlobalUtility.print(TAG, "error: $string")
                }
            })
        LocationHelper.isAddressEnabled(true)

    }
    override fun onDestroy() {
        super.onDestroy()
        LocationHelper.stopLocationUpdates()
    }
}

