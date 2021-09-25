package com.webaddicted.kotlinproject.view.dialog

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.DialogImagePickerBinding
import com.webaddicted.kotlinproject.global.annotationdef.MediaPickerType
import com.webaddicted.kotlinproject.global.common.*
import com.webaddicted.kotlinproject.view.base.BaseDialog
import com.webaddicted.kotlinproject.view.interfac.OnImageActionListener
import org.koin.android.ext.android.inject
import java.io.File


@SuppressLint("ValidFragment")
class ImagePickerDialog : BaseDialog() {
    private lateinit var mBinding: DialogImagePickerBinding
    private val mediaPicker: MediaPickerUtils by inject()
    companion object {
        private var fileType: Int = MediaPickerType.CAPTURE_IMAGE
        val TAG = ImagePickerDialog::class.java.simpleName
        private lateinit var onActionListener: OnImageActionListener
        fun dialog(
            @MediaPickerType.MediaType fileType: Int,
            onActionListner: OnImageActionListener
        ): ImagePickerDialog {
            this.fileType = fileType
            onActionListener = onActionListner
            return ImagePickerDialog()
        }
    }

    override fun getLayout(): Int {
        return R.layout.dialog_image_picker
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as DialogImagePickerBinding
        init()
        clickListener()
    }

    /**
     * initialize view
     */
    private fun init() {
        when (fileType) {
            MediaPickerType.CAPTURE_IMAGE-> {
                mBinding.linearPhoto.visible()
            }
            MediaPickerType.SCAN_DL-> {
                mBinding.linearPhoto.gone()
            }

        }
    }

    /**
     * widget click listener
     */
    private fun clickListener() {
        mBinding.relativePhotoLibrary.setOnClickListener(this)
        mBinding.relativeTakePhoto.setOnClickListener(this)
        mBinding.txtCancel.setOnClickListener(this)
        mBinding.parent.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        dialog?.let { DialogUtil.fullScreenDialogBound(it) }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.relative_take_photo -> mediaImagePicker(MediaPickerType.CAPTURE_IMAGE)
            R.id.relative_photo_library  -> mediaImagePicker(MediaPickerType.SELECT_IMAGE)
            R.id.txt_cancel, R.id.parent -> dismiss()
        }
    }
    private fun mediaImagePicker(@MediaPickerType.MediaType captureImage: Int) {
        mediaPicker.selectMediaOption(requireActivity(),
            captureImage,
            FileHelper.subFolder(),
            object : MediaPickerUtils.ImagePickerListener {
                override fun imagePath(filePath: List<File>) {
                    dismiss()
                    onActionListener.onAcceptClick(filePath)
                }
            })
    }

}
