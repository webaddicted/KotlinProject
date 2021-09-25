package com.webaddicted.kotlinproject.view.deviceinfo

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Camera
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Build
import android.os.Bundle
import android.util.Range
import android.util.Rational
import android.util.Size
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevCameraBinding
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.camera.CameraBean
import com.webaddicted.kotlinproject.view.adapter.CameraAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlinx.coroutines.*

class CameraFrm : BaseFragment() {
    private lateinit var cameraList: ArrayList<CameraBean>
    private lateinit var mAdapter: CameraAdapter
    private lateinit var camera: Camera
    private lateinit var mBinding: FrmDevCameraBinding
    private lateinit var cameraManager: CameraManager

    companion object {
        val TAG = CameraFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): CameraFrm {
            val fragment = CameraFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_dev_camera
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmDevCameraBinding
        init()
        clickListener()
    }

    private fun init() {
        cameraList = ArrayList()
        setAdapter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            camera = Camera()
            if (cameraManager.cameraIdList.size >= 2) mBinding.llParentCamera.visible()
            else mBinding.llParentCamera.gone()
            mBinding.rvCamera.layoutManager = LinearLayoutManager(activity)
            mBinding.rvCamera.hasFixedSize()
            checkCameraPermission("1")
        }
    }

    private fun clickListener() {
        mBinding.txtFrontCamera.setOnClickListener(this)
        mBinding.txtRearCamera.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.txt_rear_camera -> {
                checkCameraPermission("1")
                tabSelector(mBinding.txtRearCamera, mBinding.txtFrontCamera)
            }
            R.id.txt_front_camera -> {
                checkCameraPermission("0")
                tabSelector(mBinding.txtFrontCamera, mBinding.txtRearCamera)
            }
        }
    }

    private fun setAdapter() {
        mAdapter = CameraAdapter(cameraList)
        mBinding.rvCamera.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.rvCamera.adapter = mAdapter
    }

    private fun checkCameraPermission(id: String) {
        val multiplePermission = ArrayList<String>()
        multiplePermission.add(Manifest.permission.CAMERA)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            multiplePermission,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    GlobalScope.launch(Dispatchers.Main + Job()) {
                        val appList = withContext(Dispatchers.Default) {
                            fetchCameraCharacteristics(cameraManager, id)
                        }
                        mAdapter.notifyAdapter(appList)
                    }
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {

                }
            })
    }

    private fun tabSelector(textview1: TextView, textview2: TextView) {
        /*** Set text color */
        textview1.setTextColor(ContextCompat.getColor(mActivity, R.color.white_par))
        textview2.setTextColor(ContextCompat.getColor(mActivity, R.color.black_par))

        /*** Background color */
        textview1.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.app_color_par))
        textview2.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transprant))

        /*** Set background drawable */
        textview1.setBackgroundResource(R.drawable.rectangle_fill)
        textview2.setBackgroundResource(R.drawable.rectangle_unfill)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun fetchCameraCharacteristics(cameraManager: CameraManager, ids: String): java.util.ArrayList<CameraBean> {
        val lists = java.util.ArrayList<CameraBean>()
        val sb = StringBuilder()
        val characteristics = cameraManager.getCameraCharacteristics(ids)
        for (key in characteristics.keys) {
            sb.append(key.name).append("=").append(getCharacteristicsValue(key, characteristics))
                .append("\n\n")
            val keyNm = key.name.split(".")
            if (getCharacteristicsValue(key, characteristics) != "") {
                if (key.name.split(".").size == 4) {
                    lists.add(CameraBean(keyNm[3], getCharacteristicsValue(key, characteristics)))
                } else {
                    lists.add(CameraBean(keyNm[2], getCharacteristicsValue(key, characteristics)))
                }
            }
        }
        return lists
    }

    @SuppressLint("NewApi")
    @Suppress("UNCHECKED_CAST")
    private fun <T> getCharacteristicsValue(
        key: CameraCharacteristics.Key<T>,
        characteristics: CameraCharacteristics
    ): String {
        val values = mutableListOf<String>()
        when {
            CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES == key -> {
                val modes = characteristics.get(key) as IntArray
                modes.forEach {
                    when (it) {
                        CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_OFF -> values.add("Off")
                        CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_FAST -> values.add("Fast")
                        CameraCharacteristics.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY -> values.add(
                            "High Quality"
                        )
                    }
                }
            }
            CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES == key -> {
                val modes = characteristics.get(key) as IntArray
                modes.forEach {
                    when (it) {
                        CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_OFF -> values.add("Off")
                        CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_AUTO -> values.add("Auto")
                        CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_50HZ -> values.add("50Hz")
                        CameraCharacteristics.CONTROL_AE_ANTIBANDING_MODE_60HZ -> values.add("60Hz")
                    }
                }
            }
            CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES == key -> {
                val modes = characteristics.get(key) as IntArray
                modes.forEach {
                    when (it) {
                        CameraCharacteristics.CONTROL_AE_MODE_OFF -> values.add("Off")
                        CameraCharacteristics.CONTROL_AE_MODE_ON -> values.add("On")
                        CameraCharacteristics.CONTROL_AE_MODE_ON_ALWAYS_FLASH -> values.add("Always Flash")
                        CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH -> values.add("Auto Flash")
                        CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE -> values.add("Auto Flash Redeye")
                    }
                }
            }
            CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES == key -> {
                val ranges = characteristics.get(key) as Array<Range<Int>>
                ranges.forEach {
                    values.add(getRangeValue(it))
                }

            }
            CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE == key -> {
                val range = characteristics.get(key) as Range<Int>
                values.add(getRangeValue(range))
            }
            CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP == key -> {
                val step = characteristics.get(key) as Rational
                values.add(step.toString())
            }/* else if (CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE == key) {
                val available = characteristics.get(key)
                values.add(available.toString())
            } */
            CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES == key -> {
                val modes = characteristics.get(key) as IntArray
                modes.forEach {
                    when (it) {
                        CameraCharacteristics.CONTROL_AF_MODE_OFF -> values.add("Off")
                        CameraCharacteristics.CONTROL_AF_MODE_AUTO -> values.add("Auto")
                        CameraCharacteristics.CONTROL_AF_MODE_EDOF -> values.add("EDOF")
                        CameraCharacteristics.CONTROL_AF_MODE_MACRO -> values.add("Macro")
                        CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_PICTURE -> values.add("Continous Picture")
                        CameraCharacteristics.CONTROL_AF_MODE_CONTINUOUS_VIDEO -> values.add("Continous Video")
                    }
                }
            }
            CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS == key -> {
                val effects = characteristics.get(key) as IntArray
                effects.forEach {
                    values.add(
                        when (it) {
                            CameraCharacteristics.CONTROL_EFFECT_MODE_OFF -> "Off"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_AQUA -> "Aqua"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_BLACKBOARD -> "Blackboard"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_MONO -> "Mono"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_NEGATIVE -> "Negative"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_POSTERIZE -> "Posterize"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_SEPIA -> "Sepia"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_SOLARIZE -> "Solarize"
                            CameraCharacteristics.CONTROL_EFFECT_MODE_WHITEBOARD -> "Whiteboard"
                            else -> {
                                "Unkownn $it"
                            }
                        }
                    )
                }
            } /*else if (CameraCharacteristics.CONTROL_AVAILABLE_MODES == key) {
                val modes = characteristics.get(key) as IntArray
                modes.forEach {
                    values.add(when (it) {
                        CameraCharacteristics.CONTROL_MODE_OFF -> "Off"
                        CameraCharacteristics.CONTROL_MODE_OFF_KEEP_STATE -> "Off Keep State"
                        CameraCharacteristics.CONTROL_MODE_AUTO -> "Auto"
                        CameraCharacteristics.CONTROL_MODE_USE_SCENE_MODE -> "Use Scene Mode"
                        else -> {
                            "Unkownn ${it}"
                        }
                    })
                }
            }*/
            CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES == key -> {
                val modes = characteristics.get(key) as IntArray
                modes.forEach {
                    values.add(
                        when (it) {
                            CameraCharacteristics.CONTROL_SCENE_MODE_DISABLED -> "Disabled"
                            CameraCharacteristics.CONTROL_SCENE_MODE_ACTION -> "Action"
                            CameraCharacteristics.CONTROL_SCENE_MODE_BARCODE -> "Barcode"
                            CameraCharacteristics.CONTROL_SCENE_MODE_BEACH -> "Beach"
                            CameraCharacteristics.CONTROL_SCENE_MODE_CANDLELIGHT -> "Candlelight"
                            CameraCharacteristics.CONTROL_SCENE_MODE_FACE_PRIORITY -> "Face Priority"
                            CameraCharacteristics.CONTROL_SCENE_MODE_FIREWORKS -> "Fireworks"
                            CameraCharacteristics.CONTROL_SCENE_MODE_HDR -> "HDR"
                            CameraCharacteristics.CONTROL_SCENE_MODE_LANDSCAPE -> "Landscape"
                            CameraCharacteristics.CONTROL_SCENE_MODE_NIGHT -> "Night"
                            CameraCharacteristics.CONTROL_SCENE_MODE_NIGHT_PORTRAIT -> "Night Portrait"
                            CameraCharacteristics.CONTROL_SCENE_MODE_PARTY -> "Party"
                            CameraCharacteristics.CONTROL_SCENE_MODE_PORTRAIT -> "Portrait"
                            CameraCharacteristics.CONTROL_SCENE_MODE_SNOW -> "Snow"
                            CameraCharacteristics.CONTROL_SCENE_MODE_SPORTS -> "Sports"
                            CameraCharacteristics.CONTROL_SCENE_MODE_STEADYPHOTO -> "Steady Photo"
                            CameraCharacteristics.CONTROL_SCENE_MODE_SUNSET -> "Sunset"
                            CameraCharacteristics.CONTROL_SCENE_MODE_THEATRE -> "Theatre"
                            CameraCharacteristics.CONTROL_SCENE_MODE_HIGH_SPEED_VIDEO -> "High Speed Video"
                            else -> {
                                "Unkownn $it"
                            }
                        }
                    )
                }
            }
            CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES == key -> {
                val modes = characteristics.get(key) as IntArray
                modes.forEach {
                    values.add(
                        when (it) {
                            CameraCharacteristics.CONTROL_VIDEO_STABILIZATION_MODE_ON -> "On"
                            CameraCharacteristics.CONTROL_VIDEO_STABILIZATION_MODE_OFF -> "Off"
                            else -> {
                                "Unkownn $it"
                            }
                        }
                    )
                }
            }
            CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES == key -> {

            } /*else if (CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE == key) {

            } */
            CameraCharacteristics.CONTROL_MAX_REGIONS_AE == key -> {

            }
            CameraCharacteristics.CONTROL_MAX_REGIONS_AF == key -> {

            }
            CameraCharacteristics.CONTROL_MAX_REGIONS_AWB == key -> {

            } /*else if (CameraCharacteristics.CONTROL_POST_RAW_SENSITIVITY_BOOST_RANGE == key) {

            } else if (CameraCharacteristics.DEPTH_DEPTH_IS_EXCLUSIVE == key) {

            }*/
            CameraCharacteristics.EDGE_AVAILABLE_EDGE_MODES == key -> {

            }
            CameraCharacteristics.FLASH_INFO_AVAILABLE == key -> {

            }
            CameraCharacteristics.HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES == key -> {

            }
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL == key -> {

            }
            CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES == key -> {

            }
            CameraCharacteristics.LENS_FACING == key -> {
                val facing = characteristics.get(key)
                values.add(
                    when (facing) {
                        CameraCharacteristics.LENS_FACING_BACK -> "Back"
                        CameraCharacteristics.LENS_FACING_FRONT -> "Front"
                        CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
                        else -> "Unkown"
                    }
                )
            }
            CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES == key -> {

            }
            CameraCharacteristics.LENS_INFO_AVAILABLE_FILTER_DENSITIES == key -> {

            }
            CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS == key -> {

            }
            CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION == key -> {

            }
            CameraCharacteristics.LENS_INFO_FOCUS_DISTANCE_CALIBRATION == key -> {

            }
            CameraCharacteristics.LENS_INFO_HYPERFOCAL_DISTANCE == key -> {

            }
            CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE == key -> {

            }/* else if (CameraCharacteristics.LENS_INTRINSIC_CALIBRATION == key) {

            } else if (CameraCharacteristics.LENS_POSE_ROTATION == key) {

            } /**/else if (CameraCharacteristics.LENS_POSE_TRANSLATION == key) {

            } /**/else if (CameraCharacteristics.LENS_RADIAL_DISTORTION == key) {

            }*//* else if (CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES == key) {

            } *//*else if (CameraCharacteristics.REPROCESS_MAX_CAPTURE_STALL == key) {

            } */
            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES == key -> {

            }
            CameraCharacteristics.REQUEST_MAX_NUM_INPUT_STREAMS == key -> {

            }
            CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC == key -> {

            }
            CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING == key -> {

            }
            CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW == key -> {

            }
            CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT == key -> {

            }
            CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH == key -> {

            }
            CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM == key -> {

            }
            CameraCharacteristics.SCALER_CROPPING_TYPE == key -> {

            }
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP == key -> {
                val map = characteristics.get(key) as StreamConfigurationMap
                val outputFormats = map.outputFormats
                outputFormats.forEach {
                    values.add(printOutputFormat(it, map))
                }


            }
            CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES == key -> {

            }
            CameraCharacteristics.SENSOR_BLACK_LEVEL_PATTERN == key -> {

            }
            CameraCharacteristics.SENSOR_CALIBRATION_TRANSFORM1 == key -> {

            }
            CameraCharacteristics.SENSOR_CALIBRATION_TRANSFORM2 == key -> {

            }
            CameraCharacteristics.SENSOR_COLOR_TRANSFORM1 == key -> {

            }
            CameraCharacteristics.SENSOR_COLOR_TRANSFORM2 == key -> {

            }
            CameraCharacteristics.SENSOR_FORWARD_MATRIX1 == key -> {

            }
            CameraCharacteristics.SENSOR_FORWARD_MATRIX2 == key -> {

            }
            CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE == key -> {

            }
            CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT == key -> {

            }
            CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE == key -> {

            } /*else if (CameraCharacteristics.SENSOR_INFO_LENS_SHADING_APPLIED == key) {

            } */
            CameraCharacteristics.SENSOR_INFO_MAX_FRAME_DURATION == key -> {

            }
            CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE == key -> {

            }
            CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE == key -> {

            } /*else if (CameraCharacteristics.SENSOR_INFO_PRE_CORRECTION_ACTIVE_ARRAY_SIZE == key) {

            }*/
            CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE == key -> {

            }
            CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE == key -> {

            }
            CameraCharacteristics.SENSOR_INFO_WHITE_LEVEL == key -> {

            }
            CameraCharacteristics.SENSOR_MAX_ANALOG_SENSITIVITY == key -> {

            } /*else if (CameraCharacteristics.SENSOR_OPTICAL_BLACK_REGIONS == key) {

            }*/
            CameraCharacteristics.SENSOR_ORIENTATION == key -> {
                val orientation = characteristics.get(key) as Int
                values.add(orientation.toString())

            }
            CameraCharacteristics.SENSOR_REFERENCE_ILLUMINANT1 == key -> {

            }
            CameraCharacteristics.SENSOR_REFERENCE_ILLUMINANT2 == key -> {

            }/* else if (CameraCharacteristics.SHADING_AVAILABLE_MODES == key) {

            }*/
            CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES == key -> {

            }
            CameraCharacteristics.STATISTICS_INFO_AVAILABLE_HOT_PIXEL_MAP_MODES == key -> {

            }/* else if (CameraCharacteristics.STATISTICS_INFO_AVAILABLE_LENS_SHADING_MAP_MODES == key) {

            }*/
            CameraCharacteristics.SYNC_MAX_LATENCY == key -> {

            }
            CameraCharacteristics.TONEMAP_AVAILABLE_TONE_MAP_MODES == key -> {

            }
            CameraCharacteristics.TONEMAP_MAX_CURVE_POINTS == key -> {

            }
        }
        values.sort()
        return if (values.isEmpty()) "" else join(", ", values)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun printOutputFormat(outputFormat: Int, map: StreamConfigurationMap): String {
        val formatName = getImageFormat(outputFormat)
        val outputSizes = map.getOutputSizes(outputFormat)
        val outputSizeValues = mutableListOf<String>()
        outputSizes.forEach {
            val mp = getMegaPixel(it)
            outputSizeValues.add("${it.width}x${it.height} (${mp}MP)")
        }

        val sizesString = join(", ", outputSizeValues)

        return "\n$formatName -> [$sizesString]"
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getMegaPixel(size: Size): String {
        val mp = (size.width * size.height) / 1000000f
        return String.format("%.1f", mp)
    }

    private fun getImageFormat(format: Int): String {
        return when (format) {
            ImageFormat.DEPTH16 -> "DEPTH16"
            ImageFormat.DEPTH_POINT_CLOUD -> "DEPTH_POINT_CLOUD"
            ImageFormat.FLEX_RGBA_8888 -> "FLEX_RGBA_8888"
            ImageFormat.FLEX_RGB_888 -> "FLEX_RGB_888"
            ImageFormat.JPEG -> "JPEG"
            ImageFormat.NV16 -> "NV16"
            ImageFormat.NV21 -> "NV21"
            ImageFormat.PRIVATE -> "PRIVATE"
            ImageFormat.RAW10 -> "RAW10"
            ImageFormat.RAW12 -> "RAW12"
            ImageFormat.RAW_PRIVATE -> "RAW_PRIVATE"
            ImageFormat.RAW_SENSOR -> "RAW_SENSOR"
            ImageFormat.RGB_565 -> "RGB_565"
            ImageFormat.YUV_420_888 -> "YUV_420_888"
            ImageFormat.YUV_422_888 -> "YUV_422_888"
            ImageFormat.YUV_444_888 -> "YUV_444_888"
            ImageFormat.YUY2 -> "YUY2"
            ImageFormat.YV12 -> "YV12"
            else -> "Unkown"
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun <T : Comparable<T>> getRangeValue(range: Range<T>): String {
        return "[${range.lower},${range.upper}]"
    }

    private fun <T> join(delimiter: String, elements: Collection<T>?): String {
        if (null == elements || elements.isEmpty()) {
            return ""
        }

        val sb = StringBuilder()
        val iter = elements.iterator()
        while (iter.hasNext()) {
            val element = iter.next()
            sb.append(element.toString())

            if (iter.hasNext()) {
                sb.append(delimiter)
            }
        }

        return sb.toString()
    }
}
