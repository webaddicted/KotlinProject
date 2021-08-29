package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivitySpechTextBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseActivity
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class SpeechTextActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySpechTextBinding
    private var mTextToSpeechListener: TextToSpeech? = null
    val REQUEST_CODE_SPEECH_INPUT = 5003

    companion object {
        val TAG: String = SpeechTextActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, SpeechTextActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_spech_text
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivitySpechTextBinding
        init()
        clickListener()
    }

    private fun init() {
        mTextToSpeechListener =
            TextToSpeech(this) { mTextToSpeechListener?.language = Locale.UK }
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.speech_to_text)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnTextToSpeech.setOnClickListener(this)
        mBinding.imgSpeechToText.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> onBackPressed()
            R.id.btn_text_to_speech -> {
                val edtText = mBinding.edtText.text.toString()
                mTextToSpeechListener?.speak(edtText, TextToSpeech.QUEUE_FLUSH, null)
            }
            R.id.img_speech_to_text -> speechToTextOutput()
        }
    }

    /**
     * Showing google speech input dialog
     */
    private fun speechToTextOutput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something")
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (exp: ActivityNotFoundException) {
            Lg.d(TAG, "speechToTextOutput: $exp")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SPEECH_INPUT -> {
                    val resultSpeech = data!!
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (resultSpeech != null && resultSpeech.size > 0) {
                        mBinding.txtSpeechToText.text = resultSpeech.get(0)
                    }
                }
            }
        }
    }

}