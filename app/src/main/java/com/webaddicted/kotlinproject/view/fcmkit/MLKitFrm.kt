package com.webaddicted.kotlinproject.view.fcmkit

//import android.content.DialogInterface
//import android.graphics.Rect
//import android.net.Uri
import android.os.Bundle
import android.view.View
//import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
//import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
//import com.google.firebase.ml.vision.FirebaseVision
//import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
//import com.google.firebase.ml.vision.common.FirebaseVisionImage
//import com.google.firebase.ml.vision.common.FirebaseVisionPoint
//import com.google.firebase.ml.vision.face.FirebaseVisionFace
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
//import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
//import com.google.firebase.ml.vision.objects.FirebaseVisionObject
//import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmFcmMlKitBinding
//import com.webaddicted.kotlinproject.global.annotationdef.MediaPickerType
import com.webaddicted.kotlinproject.global.common.*
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.dialog.ImagePickerDialog
//import com.webaddicted.kotlinproject.view.interfac.OnImageActionListener
//import java.io.File
//import java.util.*
//import kotlin.collections.ArrayList


class MLKitFrm : BaseFragment() {
    private var title: String? = ""
    private lateinit var imgPickerDialog: ImagePickerDialog
    private lateinit var mBinding: FrmFcmMlKitBinding

    companion object {
        val TAG = MLKitFrm::class.java.simpleName
        const val MLKIT_TYPE = "MLKIT_TYPE"
        fun getInstance(bundle: Bundle): MLKitFrm {
            val fragment = MLKitFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_fcm_ml_kit
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmFcmMlKitBinding
        GlobalUtility.showToast("ML kit increase size of apk around 98MB.\nso i comment code if you need then uncommentcode.")
//        init()
//        clickListener()
    }
}
//    private fun init() {
//        if (arguments?.containsKey(MLKIT_TYPE)!!) {
//            title = arguments?.getString(MLKIT_TYPE)
//        }
//        mBinding.toolbar.imgNavRight.gone()
//        mBinding.toolbar.txtToolbarTitle.text = title
//
//        when (title) {
//            getString(R.string.face_detection), getString(R.string.object_detection) -> {
//                mBinding.imgCloud.gone()
//                mBinding.txtCloudResult.gone()
//                mBinding.btnCloud.gone()
//            }
//            getString(R.string.landmark_recognition) -> {
//                mBinding.imgOnDevice.gone()
//                mBinding.txtOnDeviceResult.gone()
//                mBinding.btnOnDevice.gone()
//            }
//            getString(R.string.barcode_scanner) -> {
//                mBinding.imgOnDevice.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        mActivity,
//                        R.drawable.barcode_type
//                    )
//                )
//                mBinding.imgCloud.gone()
//                mBinding.txtCloudResult.gone()
//                mBinding.btnCloud.gone()
//            }
//            getString(R.string.language_id) -> {
//                mBinding.edtText.visible()
//                mBinding.imgOnDevice.gone()
//                mBinding.imgCloud.gone()
//                mBinding.txtCloudResult.gone()
//                mBinding.btnCloud.gone()
//            }
//            getString(R.string.device_translation) -> {
//                mBinding.edtText.visible()
//                mBinding.linearLangTrans.visible()
//                mBinding.imgOnDevice.gone()
//                mBinding.imgCloud.gone()
//                mBinding.txtCloudResult.gone()
//                mBinding.btnCloud.gone()
//            }
//            getString(R.string.smart_reply) -> {
//                mBinding.edtText.visible()
//                mBinding.imgOnDevice.gone()
//                mBinding.imgCloud.gone()
//                mBinding.txtCloudResult.gone()
//                mBinding.btnCloud.gone()
//            }
//        }
//    }
//
//    private fun clickListener() {
//        mBinding.toolbar.imgNavLeft.setOnClickListener(this)
//        mBinding.btnOnDevice.setOnClickListener(this)
//        mBinding.btnCloud.setOnClickListener(this)
//        mBinding.txtDestLang.setOnClickListener(this)
//    }
//
//    override fun onClick(v: View) {
//        super.onClick(v)
//        when (v.id) {
//            R.id.img_nav_left -> (activity as FcmFoodHomeActivity).openCloseDrawer(true)
//            R.id.btn_on_device -> {
//                when (title) {
//                    getString(R.string.language_id) -> languageIdOnDevice()
//                    getString(R.string.device_translation) -> deviceTranslationOnDevice()
//                    getString(R.string.smart_reply) -> smartReplyOnDevice()
//                    else -> requestCamera(MediaPickerType.CAPTURE_IMAGE, true)
//                }
//            }
//            R.id.btn_cloud -> requestCamera(MediaPickerType.CAPTURE_IMAGE, false)
//            R.id.txt_dest_lang -> {
//                DialogUtil.getSingleChoiceDialog(mActivity,
//                    resources.getString(R.string.select_language),
//                    FirebaseTranslateLanguage.getAllLanguages().toList(),
//                    DialogInterface.OnClickListener { dialog, position ->
//                        if (position > 0) {
//                            activity?.showToast(
//                                FirebaseTranslateLanguage.getAllLanguages()
//                                    .toList()[position - 1].toString()
//                            )
//                            mBinding.txtDestLang.text =
//                                FirebaseTranslateLanguage.languageCodeForLanguage(
//                                    FirebaseTranslateLanguage.getAllLanguages().toList()[position]
//                                )
//                        }
//                        dialog.dismiss()
//                    },
//                    DialogInterface.OnClickListener { dialog, position -> dialog.dismiss() })
//
//            }//requestCamera(MediaPickerType.CAPTURE_IMAGE, false)
//
//        }
//    }
//
//    private fun requestCamera(
//        @MediaPickerType.MediaType captureImage: Int,
//        isOnDevice: Boolean
//    ) {
//        imgPickerDialog = ImagePickerDialog.dialog(captureImage,
//            object : OnImageActionListener {
//                override fun onAcceptClick(file: List<File>) {
//                    when (title) {
//                        getString(R.string.text_recognizer) -> {
//                            if (isOnDevice) textRecognizerOnDevice(file[0])
//                            else textRecognizerCloud(file[0])
//                        }
//                        getString(R.string.face_detection) -> faceDetectionOnDevice(file[0])
//                        getString(R.string.object_detection) -> objectDetectionOnDevice(file[0])
//                        getString(R.string.image_label) -> {
//                            if (isOnDevice) imageLabelOnDevice(file[0])
//                            else imageLabelCloud(file[0])
//                        }
//                        getString(R.string.barcode_scanner) -> barcodeScannerOnDevice(file[0])
//                        getString(R.string.landmark_recognition) -> landmarkRegCloud(file[0])
//                    }
//                }
//            })
//        fragmentManager?.let { imgPickerDialog.show(it, ImagePickerDialog.TAG) }
//    }
//
//    private fun textRecognizerOnDevice(file: File) {
//        mBinding.txtOnDeviceResult.text = ""
//        mBinding.imgOnDevice.showImage(file, getPlaceHolder(3))
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
//        detector.processImage(image).addOnSuccessListener { firebaseVisionText ->
//            if (firebaseVisionText.textBlocks.size == 0) {
//                mBinding.txtOnDeviceResult.text = getString(R.string.txt_no_data_found)
//            } else {
//                firebaseVisionText.textBlocks.forEachIndexed { index, textBlock ->
//                    textBlock.recognizedLanguages.forEachIndexed { index, recognizedLanguage ->
//                        mBinding.txtOnDeviceResult.append("Language : ${recognizedLanguage.languageCode}")
//                    }
//                    mBinding.txtOnDeviceResult.append("confidence : ${textBlock.confidence}%\n ${textBlock.text}\n\n")
//                }
//            }
//        }
//            .addOnFailureListener {
//                mBinding.txtOnDeviceResult.text = "Failed\n ${it.message}"
//            }
//    }
//
//    private fun textRecognizerCloud(file: File) {
//        mBinding.txtCloudResult.text = ""
//        mBinding.imgCloud.showImage(file, getPlaceHolder(3))
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val detector = FirebaseVision.getInstance().cloudDocumentTextRecognizer
//        detector.processImage(image).addOnSuccessListener { firebaseVisionCloudText ->
//            firebaseVisionCloudText.blocks.forEachIndexed { blocksindex, block ->
//                block.paragraphs.forEachIndexed { paragraphsindex, paragraph ->
//                    paragraph.words.forEachIndexed { wordsindex, word ->
//                        word.symbols.forEachIndexed { symbolsindex, symbol ->
//                            Log.d(
//                                TAG,
//                                "onResponse: extractCloudText: recognizedText-> $symbol"
//                            )
//                        }
//                    }
//                }
//            }
////                val blocks = firebaseVisionCloudText.blocks
////                for (i in blocks.indices) {
////                    val paragraphs = blocks[i].paragraphs
////                    for (j in paragraphs.indices) {
////                        val words = paragraphs[j].words
////                        for (l in words.indices) {
////                            val symbols = words[l].symbols
////                            for (m in symbols.indices) {
////
////                            }
////                        }
////                    }
////                }
//        }.addOnFailureListener { mBinding.txtCloudResult.text = "Failed\n ${it.message}" }
//    }
//
//    private fun faceDetectionOnDevice(file: File) {
//        mBinding.txtOnDeviceResult.text = ""
//        mBinding.imgOnDevice.showImage(file, getPlaceHolder(3))
//        val options = FirebaseVisionFaceDetectorOptions.Builder()
//            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
//            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
//            .build()
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
//        detector.detectInImage(image)
//            .addOnSuccessListener { results ->
//                val stringBuilder = StringBuilder()
//                stringBuilder.append("Total face detected : ${results.size}\n\n")
//                results.forEachIndexed { index, face ->
//                    stringBuilder.append("===================================\n")
//                    val bounds = face.boundingBox
//                    val rotY =
//                        face.headEulerAngleY // Head is rotated to the right rotY degrees
//                    val rotZ =
//                        face.headEulerAngleZ // Head is tilted sideways rotZ degrees
//
//                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
//                    // nose available):
//                    val leftEarPos: FirebaseVisionPoint
//                    val leftCheek: FirebaseVisionPoint
//                    val rightCheek: FirebaseVisionPoint
//                    val smileProb: Float
//                    val rightEyeOpenProb: Float
//                    val leftEyeOpenProb: Float
//                    val id: Int
//                    // If face tracking was enabled:
//                    if (face.trackingId != FirebaseVisionFace.INVALID_ID) {
//                        id = face.trackingId
//                        stringBuilder.append("ID : $id\n")
//                    }
//                    val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)
//                    if (leftEar != null) {
//                        leftEarPos = leftEar.position
//                        stringBuilder.append("Left Ear OpenProb :\n     X : ${leftEarPos.x}\n     Y : ${leftEarPos.y}\n     Z : ${leftEarPos.z}\n")
//                    }
//                    val LEFT_CHEEK = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK)
//                    if (LEFT_CHEEK != null) {
//                        leftCheek = LEFT_CHEEK.position
//                        stringBuilder.append("Left Cheek Prob :\n     X : ${leftCheek.x}\n     Y : ${leftCheek.y}\n     Z : ${leftCheek.z}\n")
//                    }
//                    val RIGHT_CHEEK = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK)
//                    if (RIGHT_CHEEK != null) {
//                        rightCheek = RIGHT_CHEEK.position
//                        stringBuilder.append(
//                            "Right Cheek Prob :\n     X : ${rightCheek.x}\n     Y : ${rightCheek.y}\n     Z : ${rightCheek.z}\n"
//                        )
//                    }
//                    if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
//                        rightEyeOpenProb = face.rightEyeOpenProbability
//                        stringBuilder.append("Right Eye OpenProb : $rightEyeOpenProb%\n")
//                    }
//                    if (face.leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
//                        leftEyeOpenProb = face.leftEyeOpenProbability
//                        stringBuilder.append("Left Eye OpenProb : $leftEyeOpenProb%\n")
//                    }
//
//                    // If classification was enabled:
//                    if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
//                        smileProb = face.smilingProbability
//                        stringBuilder.append("Smile Prob : $smileProb%\n\n\n")
//                    }
//                }
//                mBinding.txtOnDeviceResult.text = stringBuilder.toString()
//            }
//            .addOnFailureListener { e ->
//                mBinding.txtOnDeviceResult.text = "Failure\n${e.message}"
//            }
//    }
//
//    private fun objectDetectionOnDevice(file: File) {
//        mBinding.txtOnDeviceResult.text = ""
//        mBinding.imgOnDevice.showImage(file, getPlaceHolder(3))
//        val options = FirebaseVisionObjectDetectorOptions.Builder()
//            .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
//            .enableMultipleObjects()
//            .enableClassification().build()
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val detector = FirebaseVision.getInstance().getOnDeviceObjectDetector(options)
//        detector.processImage(image).addOnSuccessListener { results ->
//            Lg.d(TAG, results.toString())
//            if (results.size > 0) {
//                results.forEachIndexed { index, obj ->
//                    val box = obj.boundingBox
//                    mBinding.txtOnDeviceResult.append("Detected object : ${index}\n")
//                    Log.d(TAG, "Detected object: ${index} ")
//                    when (obj.classificationCategory) {
//                        //Firebase only supports this much categories
//                        0 -> Log.d(TAG, "  Classification name: CATEGORY_UNKNOWN")
//                        1 -> Log.d(TAG, "  Classification name: CATEGORY_HOME_GOOD")
//                        2 -> Log.d(TAG, "  Classification name: CATEGORY_FASHION_GOOD")
//                        3 -> Log.d(TAG, "  Classification name: CATEGORY_FOOD")
//                        4 -> Log.d(TAG, "  Classification name: CATEGORY_PLACE")
//                        5 -> Log.d(TAG, "  Classification name: CATEGORY_PLANT")
//                    }
//                    mBinding.txtOnDeviceResult.append("Classification code : ${obj.classificationCategory}\n")
//                    Log.d(TAG, "  Classification code : ${obj.classificationCategory}")
//                    if (obj.classificationCategory != FirebaseVisionObject.CATEGORY_UNKNOWN) {
//                        val confidence: Int = obj.classificationConfidence!!.times(100).toInt()
//                        mBinding.txtOnDeviceResult.append("Confidence : ${confidence}%\n")
//                        Log.d(TAG, "  Confidence : ${confidence}%")
//                    }
//                    mBinding.txtOnDeviceResult.append("boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom}\n\n")
//                }
//            } else mBinding.txtOnDeviceResult.append(getString(R.string.txt_no_data_found))
//        }.addOnFailureListener { e -> mBinding.txtOnDeviceResult.text = "Failure\n${e.message}" }
//    }
//
//    private fun imageLabelOnDevice(file: File) {
//        mBinding.txtOnDeviceResult.text = ""
//        mBinding.imgOnDevice.showImage(file, getPlaceHolder(3))
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val detector = FirebaseVision.getInstance().onDeviceImageLabeler
//        detector.processImage(image).addOnSuccessListener { results ->
//            val strBuilder = StringBuilder()
//            results.forEachIndexed { index, label ->
//                strBuilder.append("Label : ${label.text}\n")
//                strBuilder.append("Confidence : ${label.confidence}%\n\n")
//            }
//            mBinding.txtOnDeviceResult.text = strBuilder.toString()
//            Lg.d(TAG, results.toString())
//        }
//            .addOnFailureListener { e -> mBinding.txtOnDeviceResult.text = "Failure\n${e.message}" }
//    }
//
//    private fun imageLabelCloud(file: File) {
//        mBinding.txtCloudResult.text = ""
//        mBinding.imgCloud.showImage(file, getPlaceHolder(3))
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val detector = FirebaseVisionCloudImageLabelerOptions.Builder()
//            .build().let { options ->
//                FirebaseVision.getInstance().getCloudImageLabeler(options)
//            }
//        detector.processImage(image).addOnSuccessListener { results ->
//            val strBuilder = StringBuilder()
//            results.forEachIndexed { index, label ->
//                strBuilder.append("Label : ${label.text}\n")
//                strBuilder.append("Confidence : ${label.confidence}%\n\n")
//            }
//            mBinding.txtOnDeviceResult.text = strBuilder.toString()
//            Lg.d(TAG, results.toString())
//        }.addOnFailureListener { e -> mBinding.txtCloudResult.text = "Failure\n${e.message}" }
//    }
//
//    private fun barcodeScannerOnDevice(file: File) {
//        showApiLoader()
//        mBinding.txtOnDeviceResult.text = ""
//        mBinding.imgOnDevice.showImage(file, getPlaceHolder(3))
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val detector = FirebaseVision.getInstance().visionBarcodeDetector
//        detector.detectInImage(image).addOnSuccessListener { results ->
//            hideApiLoader()
//            if (results.size > 0) {
//                results.forEachIndexed { index, barcode ->
//                    mBinding.txtOnDeviceResult.text = barcode.rawValue + "\n"
//                    Lg.d(TAG, "barcode : ${barcode.rawValue}")
//                }
//            } else mBinding.txtOnDeviceResult.text = getString(R.string.txt_no_data_found)
//        }.addOnFailureListener { e ->
//            hideApiLoader()
//            mBinding.txtOnDeviceResult.text = "Failure\n${e.message}"
//        }
//    }
//
//    private fun landmarkRegCloud(file: File) {
//        mBinding.txtCloudResult.text = ""
//        mBinding.imgCloud.showImage(file, getPlaceHolder(3))
//        val image = FirebaseVisionImage.fromFilePath(mActivity, Uri.fromFile(file))
//        val options = FirebaseVisionCloudDetectorOptions.Builder()
//            .setMaxResults(10)
//            .setModelType(FirebaseVisionCloudDetectorOptions.STABLE_MODEL)
//            .build()
//        val detector = FirebaseVision.getInstance().getVisionCloudLandmarkDetector(options)
//        detector.detectInImage(image).addOnSuccessListener { results ->
//            val strBuilder = StringBuilder()
//            results.forEachIndexed { index, landmark ->
//                val bounds: Rect? = landmark.boundingBox
//                val landmarkName = landmark.landmark
//                val entityId = landmark.entityId
//                val confidence = landmark.confidence
//                strBuilder.append("Entity Id : ${entityId}\n")
//                strBuilder.append("Landmark Name : ${landmarkName}\n")
//                strBuilder.append("Confidence : ${confidence}%\n")
//                landmark.locations.forEachIndexed { index, firebaseVisionLatLng ->
//                    strBuilder.append("Latitude : ${firebaseVisionLatLng.latitude}\n")
//                    strBuilder.append("Longitude : ${firebaseVisionLatLng.longitude}\n\n")
//                }
//                Lg.d(TAG, "landmark : ${landmark.toString()}")
//            }
//            mBinding.txtCloudResult.text = strBuilder.toString()
//            Lg.d(TAG, results.toString())
//        }.addOnFailureListener { e -> mBinding.txtCloudResult.text = "Failure\n${e.message}" }
//    }
//
//    private fun languageIdOnDevice() {
//        showApiLoader()
//        if (mBinding.edtText.text.toString().isEmpty())
//            mBinding.edtText.setText(getString(R.string.forcefully_enable_permission))
//        mBinding.txtOnDeviceResult.text = ""
//        val languageIdentifier =
//            FirebaseNaturalLanguage.getInstance().languageIdentification
//        languageIdentifier.identifyLanguage(mBinding.edtText.text.toString())
//            .addOnSuccessListener { languageCode ->
//                hideApiLoader()
//                if (languageCode !== null && languageCode.isNotBlank())
//                    mBinding.txtOnDeviceResult.text = "Language : $languageCode"
//                else mBinding.txtOnDeviceResult.text = "Can't identify language."
//            }.addOnFailureListener {
//                hideApiLoader()
//                mBinding.txtOnDeviceResult.text = "Failure\n${it.message}"
//            }
//    }
//
//    private fun deviceTranslationOnDevice() {
//        showApiLoader()
//        if (mBinding.edtText.text.toString().isEmpty())
//            mBinding.edtText.setText(getString(R.string.forcefully_enable_permission))
//        val options = FirebaseTranslatorOptions.Builder()
//            .setSourceLanguage(FirebaseTranslateLanguage.EN)
//            .setTargetLanguage(FirebaseTranslateLanguage.languageForLanguageCode(mBinding.txtDestLang.text.toString())!!)
//            .build()
//        val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
//
//        translator.downloadModelIfNeeded().addOnSuccessListener {
//            translator.translate(mBinding.edtText.text.toString()).addOnSuccessListener { result ->
//                hideApiLoader()
//                mBinding.txtOnDeviceResult.append(result.toString()+"\n")
//            }.addOnFailureListener {
//                hideApiLoader()
//                mBinding.txtOnDeviceResult.text = "Failure\n${it.message}"
//            }
//        }.addOnFailureListener {
//            hideApiLoader()
//            mBinding.txtOnDeviceResult.text = "Failure\n${it.message}"
//        }
//    }
//
//    private fun smartReplyOnDevice() {
//        if (mBinding.edtText.text.toString().isEmpty())
//            mBinding.edtText.setText("How are you")
//
//        mBinding.txtOnDeviceResult.text = ""
//        val conversation = ArrayList<FirebaseTextMessage>()
////        conversation.add(
////            FirebaseTextMessage.createForLocalUser(
////                "Hi", System.currentTimeMillis()
////            )
////        )
//        conversation.add(
//            FirebaseTextMessage.createForLocalUser(
//                mBinding.edtText.text.toString().toLowerCase(), System.currentTimeMillis()
//            )
//        )
//        conversation.add(
//            FirebaseTextMessage.createForRemoteUser(
//                "Are you coming back soon?",
//                System.currentTimeMillis(),
//                UUID.randomUUID().toString()
//            )
//        );
//        val smartReply = FirebaseNaturalLanguage.getInstance().smartReply
//        smartReply.suggestReplies(conversation).addOnSuccessListener { result ->
//            if (result.status === SmartReplySuggestionResult.STATUS_SUCCESS) {
//                result.suggestions.forEachIndexed { index, smartReplySuggestion ->
//                    mBinding.txtOnDeviceResult.append("Suggestion : ${smartReplySuggestion.text}\n")
//                }
//            } else if (result.status === SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
//                mBinding.txtOnDeviceResult.text =
//                    "The conversation's language isn't supported, so the\nresult doesn't contain any suggestions."
//            }
//        }.addOnFailureListener { mBinding.txtOnDeviceResult.text = "Failure\n${it.message}" }
//    }
//}

