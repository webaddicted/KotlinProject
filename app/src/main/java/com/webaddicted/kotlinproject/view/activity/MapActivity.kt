package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.view.base.BaseLocation
import com.webaddicted.kotlinproject.view.fragment.CarAnimFrm
import com.webaddicted.kotlinproject.view.fragment.GoogleMapFrm
import com.webaddicted.kotlinproject.viewModel.map.MapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class MapActivity : BaseLocation(R.layout.activity_common) {
    private lateinit var mBinding: ActivityCommonBinding
    val mapViewModel: MapViewModel by viewModel()

    companion object {
        val TAG: String = MapActivity::class.java.simpleName
        const val OPEN_FRM = "OPEN_FRM"
        fun newIntent(activity: Activity, openFrm: String?) {
            val intent = Intent(activity, MapActivity::class.java)
            intent.putExtra(OPEN_FRM, openFrm)
            activity.startActivity(intent)
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
        if (intent != null) {
            when (intent.getStringExtra(OPEN_FRM)) {
                CarAnimFrm.TAG -> navigateScreen(CarAnimFrm.TAG)
                GoogleMapFrm.TAG -> navigateScreen(GoogleMapFrm.TAG)
            }
        }
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String?) {
        var frm: Fragment? = null
        when (tag) {
            GoogleMapFrm.TAG -> frm = GoogleMapFrm.getInstance(Bundle())
            CarAnimFrm.TAG -> frm = CarAnimFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }

    override fun getCurrentLocation(location: Location, address: String?) {
        Lg.d(
            TAG,
            "lat -> " + location.latitude.toString() + "\n long -> " + location.longitude.toString()
        )
        mapViewModel.locationUpdated.postValue(location)
    }


}