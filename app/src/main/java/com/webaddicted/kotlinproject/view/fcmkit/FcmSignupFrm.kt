package com.webaddicted.kotlinproject.view.fcmkit

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
//import com.google.firebase.iid.FirebaseInstanceId
import com.webaddicted.kotlinproject.BuildConfig
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiConstant
import com.webaddicted.kotlinproject.databinding.FrmFcmSignupBinding
import com.webaddicted.kotlinproject.global.annotationdef.MediaPickerType
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.GlobalUtility.Companion.setEnableView
import com.webaddicted.kotlinproject.global.common.ValidationHelper
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.dialog.ImagePickerDialog
import com.webaddicted.kotlinproject.view.fragment.ZoomImageFrm
import com.webaddicted.kotlinproject.view.interfac.OnImageActionListener
import java.io.File
import java.util.ArrayList

class FcmSignupFrm : BaseFragment() {
    private var socialLoginRespo: FcmSocialLoginRespoBean? = null
    private var imgPickerDialog: ImagePickerDialog? = null
    private lateinit var mBinding: FrmFcmSignupBinding
    private var profileImageUrl = ""
    private var profileImageFile: File? = null

    companion object {
        val TAG = FcmSignupFrm::class.java.simpleName
        val SOCIAL_RESPO = "SOCIAL_RESPO"
        val OPEN_FROM = "OPEN_FROM"
        fun getInstance(bundle: Bundle): FcmSignupFrm {
            val fragment = FcmSignupFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_fcm_signup
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmFcmSignupBinding
        init()
        clickListener()
    }

    private fun init() {
        socialLoginRespo = FcmSocialLoginRespoBean()
        if (arguments != null && arguments?.containsKey(SOCIAL_RESPO)!!) {
            socialLoginRespo =
                arguments?.getSerializable(SOCIAL_RESPO) as FcmSocialLoginRespoBean
            mBinding.edtName.setText(socialLoginRespo?.userName)
            mBinding.edtEmail.setText(socialLoginRespo?.userEmailId)
            if (socialLoginRespo?.userImage?.length!! > 0) {
                profileImageUrl = socialLoginRespo?.userImage!!
                mBinding.imgProfile.showImage(profileImageUrl, getPlaceHolder(4))
            }
            setEnableView(mBinding.edtEmail, false)
        }
        if (!BuildConfig.IS_UAT) {
            mBinding.edtName.setText("Deepak Sharma")
            mBinding.edtMobile.setText("9024061407")
            mBinding.edtEmail.setText("sharma9024061407@gmail.com")
            mBinding.edtDob.setText("03/30/20")
            mBinding.edtPwd.setText("Deepak9950")
        }
    }

    private fun clickListener() {
        mBinding.includeBack.linearBack.setOnClickListener(this)
        mBinding.btnSignup.setOnClickListener(this)
        mBinding.txtLogin.setOnClickListener(this)
        mBinding.imgPicker.setOnClickListener(this)
        mBinding.imgProfile.setOnClickListener(this)
        mBinding.edtDob.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.include_back, R.id.txt_login -> activity?.onBackPressed()
            R.id.edt_dob -> activity?.let { GlobalUtility.getDOBDate(it, mBinding.edtDob) }
            R.id.img_picker -> requestCamera(MediaPickerType.SELECT_IMAGE)
            R.id.img_profile -> openImg()
            R.id.btn_signup -> validate()
        }
    }

    private fun requestCamera(@MediaPickerType.MediaType captureImage: Int) {
        imgPickerDialog = ImagePickerDialog.dialog(captureImage,
            object : OnImageActionListener {
                override fun onAcceptClick(file: List<File>) {
                    profileImageFile = file[0]
                    socialLoginRespo?.profileImgFile = profileImageFile
                    mBinding.imgProfile.showImage(file[0], getPlaceHolder(0))
                }
            })
        parentFragmentManager.let { imgPickerDialog?.show(it, ImagePickerDialog.TAG) }
    }

    private fun validate() {
        if (!ValidationHelper.validateBlank(
                mBinding.edtName,
                mBinding.wrapperName,
                getString(R.string.first_name_can_not_blank)
            ) ||
            !ValidationHelper.validateMobileNo(mBinding.edtMobile, mBinding.wrapperMobile) ||
            !ValidationHelper.validateEmail(mBinding.edtEmail, mBinding.wrapperEmail) ||
            !ValidationHelper.validateDob(mBinding.edtDob, mBinding.wrapperDob) ||
            !ValidationHelper.validatePwd(mBinding.edtPwd, mBinding.wrapperPwd)
        ) return
        if (profileImageUrl.isEmpty() && profileImageFile == null) {
            GlobalUtility.showToast(getString(R.string.capture_image))
            return
        }
        apiSignup()
    }

    private fun apiSignup() {
        val dbRef = getFcmDBRef(ApiConstant.FCM_DB_USERS)
        val query = dbRef.orderByChild(ApiConstant.FCM_USERS_MOBILE_NO)
            .equalTo(mBinding.edtMobile.text.toString())
        showApiLoader()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hideApiLoader()
                dbRef.removeEventListener(this)
                if (dataSnapshot.value == null) {
                    if (socialLoginRespo != null) {
                        socialLoginRespo?.dob = mBinding.edtDob.text.toString()
                        socialLoginRespo?.password = mBinding.edtPwd.text.toString()
                        socialLoginRespo?.userMobileno = mBinding.edtMobile.text.toString()
                        socialLoginRespo?.userId = mBinding.edtMobile.text.toString()
                    } else {
                        socialLoginRespo = FcmSocialLoginRespoBean().apply {
                            imei = GlobalUtility.getDeviceIMEI(mActivity)
                            userName = mBinding.edtName.text.toString()
                            userEmailId = mBinding.edtEmail.text.toString()
                            userMobileno = mBinding.edtMobile.text.toString()
                            dob = mBinding.edtDob.text.toString()
                            provider = "Manual Login"
//                            fcmToken = FirebaseInstanceId.getInstance().token
                            if (profileImageFile != null)
                                profileImgFile = profileImageFile
                        }
                    }
                    val bundle = Bundle()
                    bundle.putSerializable(SOCIAL_RESPO, socialLoginRespo)
                    navigateScreen(FcmOtpFrm.TAG, bundle)
                } else GlobalUtility.showToast(getString(R.string.mobile_no_already_exist))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                hideApiLoader()
                dbRef.removeEventListener(this)
                GlobalUtility.showToast(databaseError.message)
                dbRef.removeEventListener(this)
            }
        })
    }

    private fun navigateScreen(tag: String, bundle: Bundle) {
        var frm: Fragment? = null
        when (tag) {
            FcmOtpFrm.TAG -> frm = FcmOtpFrm.getInstance(bundle)
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

    override fun onResume() {
        super.onResume()
        addBlankSpace(mBinding.bottomSpace)
    }

    private fun openImg() {
        val bundle = Bundle()
        if (socialLoginRespo?.userImage!=null && socialLoginRespo?.userImage?.isNotEmpty()!!){
            bundle.putString(ZoomImageFrm.IMAGE_PATH, socialLoginRespo?.userImage)
            bundle.putBoolean(ZoomImageFrm.IS_LOCAL_FILE, false)
        }else if(socialLoginRespo?.profileImgFile!=null && socialLoginRespo?.profileImgFile?.exists()!!){
            bundle.putString(ZoomImageFrm.IMAGE_PATH, socialLoginRespo?.profileImgFile?.path)
            bundle.putBoolean(ZoomImageFrm.IS_LOCAL_FILE, true)
        }
        navigateScreen(ZoomImageFrm.TAG, bundle)
    }
}

