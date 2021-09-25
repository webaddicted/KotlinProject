package com.webaddicted.kotlinproject.view.fcmkit

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiConstant
import com.webaddicted.kotlinproject.databinding.FrmFcmHomeBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.model.circle.CircleGameBean
import com.webaddicted.kotlinproject.view.adapter.CircleGameAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.fragment.ZoomImageFrm
import com.webaddicted.kotlinproject.viewModel.fcmkit.FcmFoodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class FcmHomeFrm : BaseFragment(R.layout.frm_fcm_home) {
    private var categoryBean: ArrayList<CircleGameBean>? = null
    private var adapter: CircleGameAdapter? = null
    private lateinit var mBinding: FrmFcmHomeBinding
    private val viewModel: FcmFoodViewModel by viewModel()

    companion object {
        val TAG = FcmHomeFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): FcmHomeFrm {
            val fragment = FcmHomeFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmFcmHomeBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgNavRight.gone()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.home)
        categoryBean = ArrayList()
        setAdapter()
        getFoodCat()
    }


    private fun clickListener() {
        mBinding.toolbar.imgNavLeft.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_nav_left -> (activity as FcmFoodHomeActivity).openCloseDrawer(true)
        }
    }

    private fun setAdapter() {
        adapter = CircleGameAdapter(this, categoryBean)
        mBinding.includeRv.rvApps.layoutManager = LinearLayoutManager(activity)
        mBinding.includeRv.rvApps.adapter = adapter
    }

    private fun getFoodCat() {
        val dbRef = getFcmDBRef(ApiConstant.FCM_DB_CATEGORY)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dbRef.removeEventListener(this)
                for (noteDataSnapshot in dataSnapshot.children) {
                    val note: CircleGameBean? =
                        noteDataSnapshot.getValue(CircleGameBean::class.java)
                    categoryBean?.add(note!!)
                }
                mBinding.includeRv.imgNoDataFound.gone()
                adapter?.notifyAdapter(categoryBean!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                dbRef.removeEventListener(this)
            }
        })
    }

    private fun navigateScreen(tag: String?, bundle: Bundle) {
        var frm: Fragment? = null
        when (tag) {
            FcmOtpFrm.TAG -> frm = FcmOtpFrm.getInstance(bundle)
            ZoomImageFrm.TAG -> frm = ZoomImageFrm.getInstance(bundle)
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

    fun openImg(image: String?) {
        val bundle = Bundle()
        bundle.putString(ZoomImageFrm.IMAGE_PATH, image)
        bundle.putBoolean(ZoomImageFrm.IS_LOCAL_FILE, false)
        navigateScreen(ZoomImageFrm.TAG, bundle)
    }
}

