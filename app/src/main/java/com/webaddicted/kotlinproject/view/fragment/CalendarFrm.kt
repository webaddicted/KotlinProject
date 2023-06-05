package com.webaddicted.kotlinproject.view.fragment

//import com.applandeo.materialcalendarview.utils.DateUtils
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmCalendarBinding
import com.webaddicted.kotlinproject.view.base.BaseFragment

class CalendarFrm : BaseFragment(R.layout.frm_calendar) {
    private lateinit var mBinding: FrmCalendarBinding
    companion object {
        val TAG = CalendarFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): CalendarFrm {
            val fragment = CalendarFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmCalendarBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visibility = View.VISIBLE
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.calendar_title)
//        mBinding.calendarView.setDisabledDays(getDisabledDays())
//        mBinding.calendarView.setFilledDays(getFilledDays())
//        mBinding.calendarView.setOnDayClickListener({ eventDay ->
//            mBinding.txtClickData.text = ("\n selected date -> " + eventDay.calendar.time.toString()
//                    + "\nis days Enable-> " + eventDay.isEnabled +
//                    "\nis days Filled  -> " + eventDay.isFilled)
//        })
    }
    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private fun getDisabledDays(): List<Calendar> {
//        val firstDisabled = DateUtils.getCalendar()
//        firstDisabled.add(Calendar.DAY_OF_MONTH, printDifference("12/09/2019"))
//        val secondDisabled = DateUtils.getCalendar()
//        secondDisabled.add(Calendar.DAY_OF_MONTH, printDifference("09/09/2019"))
//        val thirdDisabled = DateUtils.getCalendar()
//        thirdDisabled.add(Calendar.DAY_OF_MONTH, printDifference("14/09/2019"))
//        val forthDisabled = DateUtils.getCalendar()
//         forthDisabled.add(Calendar.DAY_OF_MONTH, 2)
//        val fifthDisabled = DateUtils.getCalendar()
//         fifthDisabled.add(Calendar.DAY_OF_MONTH, 4)
//        val sixDisabled = DateUtils.getCalendar()
//         sixDisabled.add(Calendar.DAY_OF_MONTH, 6)
//        val calendars = ArrayList<Calendar>()
//        calendars.add(firstDisabled)
//        calendars.add(secondDisabled)
//        calendars.add(thirdDisabled)
//        calendars.add(forthDisabled)
//        calendars.add(fifthDisabled)
//        calendars.add(sixDisabled)
//        return calendars
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private fun getFilledDays(): List<Calendar> {
//        val firstDisabled = DateUtils.getCalendar()
//        firstDisabled.add(Calendar.DAY_OF_MONTH, printDifference("24/09/2019"))
//        val secondDisabled = DateUtils.getCalendar()
//        secondDisabled.add(Calendar.DAY_OF_MONTH, printDifference("25/09/2019"))
//        val thirdDisabled = DateUtils.getCalendar()
//        thirdDisabled.add(Calendar.DAY_OF_MONTH, printDifference("26/09/2019"))
//        val forthDisabled = DateUtils.getCalendar()
//        forthDisabled.add(Calendar.DAY_OF_MONTH, 12)
//        val fifthDisabled = DateUtils.getCalendar()
//        fifthDisabled.add(Calendar.DAY_OF_MONTH, 14)
//        val sixDisabled = DateUtils.getCalendar()
//        sixDisabled.add(Calendar.DAY_OF_MONTH, 16)
//        val calendars = ArrayList<Calendar>()
//        calendars.add(firstDisabled)
//        calendars.add(secondDisabled)
//        calendars.add(thirdDisabled)
//        calendars.add(forthDisabled)
//        calendars.add(fifthDisabled)
//        calendars.add(sixDisabled)
//
//        return calendars
//    }
//
//
//    //    public void get
//    //1 minute = 60 seconds
//    //1 hour = 60 x 60 = 3600
//    //1 day = 3600 x 24 = 86400
//    fun printDifference(endDateStr: String): Int {
//        //        DateTimeUtils obj = new DateTimeUtils();
//        val simpleDateFormat = SimpleDateFormat("dd/M/yyyy")
//        val currentDate = simpleDateFormat.format(Calendar.getInstance().time)
//        var startDate: Date? = null
//        var endDate: Date? = null
//        try {
//            startDate = simpleDateFormat.parse(currentDate)
//            endDate = simpleDateFormat.parse(endDateStr)
//            //            obj.printDifference(date1, date2);
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        //milliseconds
//        var different = endDate!!.time - startDate!!.time
//
//        println("startDate : $startDate")
//        println("endDate : $endDate")
//        println("different : $different")
//
//        val secondsInMilli: Long = 1000
//        val minutesInMilli = secondsInMilli * 60
//        val hoursInMilli = minutesInMilli * 60
//        val daysInMilli = hoursInMilli * 24
//
//        val elapsedDays = different / daysInMilli
//        different = different % daysInMilli
//
//        val elapsedHours = different / hoursInMilli
//        different = different % hoursInMilli
//
//        val elapsedMinutes = different / minutesInMilli
//        different = different % minutesInMilli
//
//        val elapsedSeconds = different / secondsInMilli
//
//        System.out.printf(
//            "%d days, %d hours, %d minutes, %d seconds%n",
//            elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
//        )
//        return Integer.parseInt(elapsedDays.toString() + "")
//    }
}

