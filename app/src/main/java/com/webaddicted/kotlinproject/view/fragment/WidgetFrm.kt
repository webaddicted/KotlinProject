package com.webaddicted.kotlinproject.view.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.MenuInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.MultiAutoCompleteTextView
import android.widget.TimePicker
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmWidgetBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment


class WidgetFrm : BaseFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var handler: Handler? = null
    private var thread: Thread? = null
    private lateinit var mBinding: FrmWidgetBinding
    private var multiArray = arrayOf(
        "America",
        "Belgium",
        "Canada",
        "Denmark",
        "England",
        "France",
        "Germany",
        "Holland",
        "India",
        "Indonesia",
        "Italy",
        "Spain"
    )

    companion object {
        val TAG = WidgetFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): WidgetFrm {
            val fragment = WidgetFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_widget
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmWidgetBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.widget_title)
        mBinding.txtMarquee.isSelected = true
        handler = Handler()
        setEditText()
        setProgressBar()
    }

    private fun clickListener() {
        mBinding.btnDataPicker.setOnClickListener(this)
        mBinding.btnTimePicker.setOnClickListener(this)
        mBinding.imgOptionMenu.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_option_menu -> showPopupMenu(mBinding.imgOptionMenu)
            R.id.btn_data_picker ->
                activity?.let { GlobalUtility.getDate(it, mBinding.txtDateValue) }
            R.id.btn_time_picker -> activity?.let { GlobalUtility.timePicker(it, this).show() }
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun setProgressBar() {
        mBinding.pbCircular.progress = 0
        mBinding.pbCircular.secondaryProgress = 100
        mBinding.pbCircular.max = 100
        mBinding.pbCircular.progressDrawable = ContextCompat.getDrawable(activity!!,R.drawable.pb_circulare)
        var progressBarStatus = 0
        thread = Thread(Runnable {
            while (progressBarStatus < 100) {
                progressBarStatus += 1
                handler?.post {
                    mBinding.pbHori.progress = progressBarStatus
                    mBinding.pbCircle.progress = progressBarStatus
                    mBinding.pbCircular.progress = progressBarStatus
                    mBinding.txtCircular.text = String.format("%d%%", progressBarStatus)
                    mBinding.seekBar.progress = progressBarStatus
                    mBinding.donutInternalStorage.progress = progressBarStatus.toFloat()
                    mBinding.arcRam.progress = progressBarStatus
                    mBinding.wavePb.setProgress(progressBarStatus)
                    mBinding.txtWaveLevel.text = String.format("%d%%", progressBarStatus)
                }
                try {
                    Thread.sleep(1600)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        })
        thread?.start()
    }

    private fun setEditText() {
        val adapter =
            ArrayAdapter(
                activity!!,
                android.R.layout.simple_list_item_1,
                multiArray
            )

        mBinding.autoCompleteTextVie.setAdapter(adapter)
        mBinding.autoCompleteTextVie.threshold = 1
        mBinding.autoCompleteTextVie.setOnItemClickListener { adapterView, view, position, l ->
            val selection = adapterView.getItemAtPosition(position) as String
            GlobalUtility.showToast(selection)
        }
        mBinding.multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        mBinding.multiAutoCompleteTextView.setAdapter(adapter)
        mBinding.multiAutoCompleteTextView.threshold = 1

        mBinding.filledExposedDropdownEditable.setAdapter(adapter)
//        mBinding.outlinedExposedDropdown.setAdapter(adapter)
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mBinding.txtDateValue.text = "$dayOfMonth/$month/$year"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mBinding.txtTimeValue.text = "$hourOfDay:$minute"
    }

    private fun showPopupMenu(view: View) { // inflate menu
        val popup = PopupMenu(activity!!, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_gridview, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.viewImage -> GlobalUtility.showToast("View Image")
                R.id.delete -> GlobalUtility.showToast("Delete item")
                else -> GlobalUtility.showToast("Other menu click")
            }
            true
        }
        popup.show()
    }
}

