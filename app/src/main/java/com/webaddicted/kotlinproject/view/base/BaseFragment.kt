package com.webaddicted.kotlinproject.view.base

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiResponse
import com.webaddicted.kotlinproject.global.common.*
import com.webaddicted.kotlinproject.global.sharedpref.PreferenceMgr
import com.webaddicted.kotlinproject.view.dialog.LoaderDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject
import java.io.File

/**
 * Created by Deepak Sharma on 15/1/19.
 */
abstract class BaseFragment(private val layoutId: Int) : Fragment(), View.OnClickListener,
    PermissionHelper.Companion.PermissionListener,
    MediaPickerUtils.ImagePickerListener {
    private lateinit var mBinding: ViewDataBinding
    private var loaderDialog: LoaderDialog? = null
    protected val mediaPicker: MediaPickerUtils by inject()
    protected val preferenceMgr: PreferenceMgr by inject()
    protected val mActivity by lazy { requireActivity() }
    protected abstract fun onBindTo(binding: ViewDataBinding?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBindTo(mBinding)
        super.onViewCreated(view, savedInstanceState)
        if (loaderDialog == null) {
            loaderDialog = LoaderDialog.dialog()
            loaderDialog?.isCancelable = false
        }
    }

    protected fun showApiLoader() {
        if (loaderDialog != null) {
            val fragment = mActivity.supportFragmentManager.findFragmentByTag(LoaderDialog.TAG)
            if (fragment != null) mActivity.supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
            loaderDialog?.show(parentFragmentManager, LoaderDialog.TAG)
        }
    }


    protected fun hideApiLoader() {
        if (loaderDialog != null && loaderDialog?.isVisible!!) loaderDialog?.dismiss()
    }

    protected fun <T> apiResponseHandler(view: View, response: ApiResponse<T>) {
        when (response.status) {
            ApiResponse.Status.LOADING -> {
                showApiLoader()
            }
            ApiResponse.Status.ERROR -> {
                hideApiLoader()
                if (response.errorMessage != null && response.errorMessage?.length!! > 0)
                    ValidationHelper.showSnackBar(view, response.errorMessage!!)
                else activity?.showToast(getString(R.string.something_went_wrong))
            }
            else -> {
                showApiLoader()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { GlobalUtility.hideKeyboard(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun eventBusListener() {
    }

    protected fun navigateFragment(
        layoutContainer: Int,
        fragment: Fragment,
        isEnableBackStack: Boolean
    ) {
        if (activity != null) {
            (activity as BaseActivity).navigateFragment(
                layoutContainer,
                fragment,
                isEnableBackStack
            )
        }
    }

    protected fun navigateAddFragment(
        layoutContainer: Int,
        fragment: Fragment,
        isEnableBackStack: Boolean
    ) {
        if (activity != null) {
            (activity as BaseActivity).navigateAddFragment(
                layoutContainer,
                fragment,
                isEnableBackStack
            )
        }
    }

    protected fun navigateChildFragment(
        layoutContainer: Int,
        fragment: Fragment,
        isEnableBackStack: Boolean
    ) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(layoutContainer, fragment)
        if (isEnableBackStack)
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onClick(v: View) {
        activity?.let { GlobalUtility.hideKeyboard(it) }
        GlobalUtility.avoidDoubleClicks(v)
        GlobalUtility.btnClickAnimation(v)
    }

    fun checkStoragePermission() {
        val multiplePermission = ArrayList<String>()
        multiplePermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        multiplePermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        multiplePermission.add(Manifest.permission.CAMERA)
        if (PermissionHelper.checkMultiplePermission(mActivity, multiplePermission)) {
            FileHelper.createApplicationFolder()
            onPermissionGranted(multiplePermission)
        } else
            PermissionHelper.requestMultiplePermission(mActivity, multiplePermission, this)
    }


    fun checkBlinkPermission() {
        val multiplePermission = ArrayList<String>()
        multiplePermission.add(Manifest.permission.CAMERA)
        multiplePermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        multiplePermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        multiplePermission.add(Manifest.permission.ACCESS_FINE_LOCATION)
        multiplePermission.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (PermissionHelper.checkMultiplePermission(
                mActivity,
                multiplePermission
            )
        ) onPermissionGranted(multiplePermission)
        else PermissionHelper.requestMultiplePermission(mActivity, multiplePermission, this)
    }

    override fun onPermissionGranted(mCustomPermission: List<String>) {
        FileHelper.createApplicationFolder()
    }


    override fun onPermissionDenied(mCustomPermission: List<String>) {
    }

    override fun imagePath(filePath: List<File>) {
    }


    fun getPlaceHolder(imageLoaderPos: Int): String {
        val imageLoader = resources.getStringArray(R.array.image_loader)
        return imageLoader[imageLoaderPos]
    }

    protected fun addBlankSpace(bottomSpace: LinearLayout) {
        KeyboardEventListener(activity as AppCompatActivity) { isKeyboardOpen: Boolean, softkeybordHeight: Int ->
            if (isKeyboardOpen)
                bottomSpace.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    softkeybordHeight
                )
            else bottomSpace.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
        }
    }

    protected fun getFcmDBRef(child: String): DatabaseReference {
        val fcmDb = FirebaseDatabase.getInstance().reference
        return fcmDb.child(child)
    }

    protected fun getFcmStorageRef(child: String): StorageReference {
        val firebaseStorage = FirebaseStorage.getInstance().reference
        return firebaseStorage.child(child)
    }
}
