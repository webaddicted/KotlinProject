package com.webaddicted.kotlinproject.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmTaskListBinding
import com.webaddicted.kotlinproject.global.annotationdef.SortListType
import com.webaddicted.kotlinproject.global.common.*
import com.webaddicted.kotlinproject.global.services.ScheduledJobService
import com.webaddicted.kotlinproject.view.activity.*
import com.webaddicted.kotlinproject.view.adapter.TaskAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.ecommerce.EcommLoginFrm
import com.webaddicted.kotlinproject.view.fcmkit.FcmFoodActivity
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class TaskFrm : BaseFragment(R.layout.frm_task_list) {
    private lateinit var mBinding: FrmTaskListBinding
    private lateinit var mHomeAdapter: TaskAdapter
    private var mTaskList: ArrayList<String>? = ArrayList()
    private var worktask = arrayOf(
        "Widgets",
        "News Api",
        "Google Map / Location",
        "Circle Game",
        "Calendar View",
        "SMS Retriever",
        "Webview",
        "Dialog",
        "Select Multiple Image",
        "Dynamic Layout & PDF",
        "Shared Preference",
        "Device Info",
        "Speech to text",
        "Animation",
        "Recycler View",
        "Expendable/Spinner List View",
        "Share",
        "Receiver",
        "Services",
        "Viewpager Tab",
        "FingerPrint",
        "Barcode",
        "Timer",
        "BlinkScan",
        "Ecommerce",
        "Navigation Drawer Both Side",
        "Navigation Drawer",
        "ScreenShot",
        "Restrict ScreenShot",
        "Digital Signature",
        "Collapse/Expend",
        "UI Design",
        "Fab Button",
        "Bottom Navigation",
        "Bottom Sheet",
        "Bottom Sheet Behav",
        "Coroutines",
        "Splash",
        "Rate App",
        "Contacts",
        "SMS",
        "Phone Image",
        "Notification",
        "Zoom Image(Touch/TwoFinger)",
        "Exo Player",
        "Exo Player Recycler View",
        "Exo Player PIP",
        "Collapse Toolbar",
        "Collapse Toolbar Behavior",
        "Location Helper",
        "Firebase",
        "Job Dispatcher",
        "Work Manager",
        "Landing Page",
        "Call Logs",
        "Car Animation"
    )
    private lateinit var showSearchView: ShowSearchView

    companion object {
        val TAG = TaskFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): TaskFrm {
            val fragment = TaskFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmTaskListBinding
        init()
    }

    private fun init() {
        mBinding.toolbar.imgBack.gone()
        mBinding.toolbar.imgProfile.visible()
        mBinding.toolbar.imgSort.visible()
        mBinding.toolbar.imgTheme.visible()
        mBinding.toolbar.imgProfile.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_search,
                null
            )
        )
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.task_title)
        mTaskList = ArrayList(listOf(*worktask))
        setAdapter()
        clickListener()
        sortList(SortListType.ASCENDING)
        showSearchView = ShowSearchView()
    }

    private fun clickListener() {
        mBinding.toolbar.imgProfile.setOnClickListener(this)
        mBinding.toolbar.imgSearchBack.setOnClickListener(this)
        mBinding.toolbar.imgSort.setOnClickListener(this)
        mBinding.toolbar.imgTheme.setOnClickListener(this)
        mBinding.toolbar.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val text = mBinding.toolbar.editTextSearch.text.toString()
                    .lowercase(Locale.getDefault())
                mHomeAdapter.filter(text)
            }


            override fun afterTextChanged(editable: Editable) {
            }
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_search_back, R.id.img_profile ->
                showSearchView.handleToolBar(
                    activity,
                    mBinding.toolbar.cardSearch,
                    mBinding.toolbar.editTextSearch
                )
//            R.id.btn_mobile_no -> {
//                val intent = Intent(Intent.ACTION_DIAL)
//                intent.data = Uri.parse("tel:" + getString(R.string.deep_mobile_no))
//                startActivity(intent)
//            }
            R.id.img_sort -> showPopupMenu(mBinding.toolbar.imgSort)
            R.id.img_theme -> appDarkTheme()
        }
    }

    private fun setAdapter() {
        mHomeAdapter = TaskAdapter(this@TaskFrm, mTaskList)
        mBinding.recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.recyclerView.adapter = mHomeAdapter
        mBinding.swipeView.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.white))
        mBinding.swipeView.setWaveColor(ContextCompat.getColor(mActivity, R.color.app_color))
        mBinding.swipeView.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                mBinding.swipeView.isRefreshing = false
            }, 1800)
        }
    }

    fun onClicks(click: String) {
        when (click) {
            "Widgets" -> navigateScreen(WidgetFrm.TAG)
            "News Api" -> navigateScreen(NewsFrm.TAG)
            "Google Map / Location" -> navigateScreen(MapActivity.TAG)
            "Circle Game" -> navigateScreen(CircleFrm.TAG)
            "Calendar View" -> navigateScreen(CalendarFrm.TAG)
            "SMS Retriever" -> navigateScreen(SmsRetrieverFrm.TAG)
            "Webview" -> navigateScreen(WebViewActivity.TAG)
            "Dialog" -> navigateScreen(DialogFrm.TAG)
            "Select Multiple Image" -> navigateScreen(ProfileFrm.TAG)
            "Dynamic Layout & PDF" -> navigateScreen(DynamicLayoutFrm.TAG)
            "Shared Preference" -> navigateScreen(SharedPrefFrm.TAG)
            "Speech to text" -> navigateScreen(SpeechTextActivity.TAG)
            "Animation" -> navigateScreen(AnimationFrm.TAG)
            "Share" -> navigateScreen(ShareDataFrm.TAG)
            "Recycler View" -> navigateScreen(RecyclerViewFrm.TAG)
            "Expendable/Spinner List View" -> navigateScreen(ExpendableFrm.TAG)
            "Receiver" -> navigateScreen(ReceiverFrm.TAG)
            "Services" -> navigateScreen(ServiceFrm.TAG)
            "Ecommerce" -> navigateScreen(EcommLoginFrm.TAG)
            "Timer" -> navigateScreen(TimerFrm.TAG)
            "Navigation Drawer Both Side" -> navigateScreen(NavBothSideDrawerActivity.TAG)
            "Navigation Drawer" -> navigateScreen(NavDrawerActivity.TAG)
            "BlinkScan" -> navigateScreen(BlinkScanFrm.TAG)
            "Coroutines" -> navigateScreen(CoroutineFrm.TAG)
            "ScreenShot" -> checkStoragePermission()
            "Splash" -> navigateScreen(SplashActivity.TAG)
            "Device Info" -> navigateScreen(DeviceInfoActivity.TAG)
            "Rate App" -> activity?.let { GlobalUtility.rateUsApp(it) }
            "Barcode" -> navigateScreen(BarcodeFrm.TAG)
            "Bottom Navigation" -> navigateScreen(BottomNavigationFrm.TAG)
            "Bottom Sheet" -> navigateScreen(BottomSheetFrm.TAG)
            "Bottom Sheet Behav" -> navigateScreen(BottomSheetBehavFrm.TAG)
            "Collapse/Expend" -> navigateScreen(CollapseExpendFrm.TAG)
            "Digital Signature" -> navigateScreen(DigitalSignatureFrm.TAG)
            "Fab Button" -> navigateScreen(FabButtonFrm.TAG)
            "Viewpager Tab" -> navigateScreen(ViewPagerTabFrm.TAG)
            "FingerPrint" -> setUpFingrePrint()
            "Restrict ScreenShot" ->
                GlobalUtility.showToast("Add window flag before setContentView\nwindow.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)")
            "Contacts" -> navigateScreen(ContactFrm.TAG)
            "SMS" -> navigateScreen(SmsFrm.TAG)
            "Phone Image" -> navigateScreen(PhoneImageFrm.TAG)
            "Notification" -> navigateScreen(NotificationFrm.TAG)
            "Zoom Image(Touch/TwoFinger)" -> navigateScreen(ZoomImageFrm.TAG)
            "Exo Player" -> navigateScreen(ExoPlayerFrm.TAG)
            "Exo Player Recycler View" -> navigateScreen(ExoPlayerRecyclerFrm.TAG)
            "Exo Player PIP" -> navigateScreen(ExoPlayerPIPActivity.TAG)
            "Collapse Toolbar" -> navigateScreen(CollapseToolbarFrm.TAG)
            "Collapse Toolbar Behavior" -> navigateScreen(CollapseToolbarBehavFrm.TAG)
            "Location Helper" -> navigateScreen(LocationHelperFrm.TAG)
            "Firebase" -> navigateScreen(FcmFoodActivity.TAG)
            "Job Dispatcher" -> startJobDispatcher()
            "Work Manager" -> navigateScreen(WorkManagerFrm.TAG)
            "Landing Page" -> navigateScreen(LandingPageFrm.TAG)
            "Call Logs" -> navigateScreen(CallLogFrm.TAG)
            "Car Animation" -> navigateScreen(CarAnimFrm.TAG)
            else -> navigateScreen(WidgetFrm.TAG)
        }
    }

    /**
     * navigate on fragment
     *
     * @param tag represent navigation activity
     */
    fun navigateScreen(tag: String?) {
        var frm: Fragment? = null
        when (tag) {
            WidgetFrm.TAG -> frm = WidgetFrm.getInstance(Bundle())
            NewsFrm.TAG -> frm = NewsFrm.getInstance(Bundle())
            MapActivity.TAG -> activity?.let { MapActivity.newIntent(it, GoogleMapFrm.TAG) }
            CircleFrm.TAG -> frm = CircleFrm.getInstance(Bundle())
            CalendarFrm.TAG -> frm = CalendarFrm.getInstance(Bundle())
            SmsRetrieverFrm.TAG -> frm = SmsRetrieverFrm.getInstance(Bundle())
            WebViewActivity.TAG -> activity?.let { WebViewActivity.newIntent(it) }
            DialogFrm.TAG -> frm = DialogFrm.getInstance(Bundle())
            ProfileFrm.TAG -> frm = ProfileFrm.getInstance(Bundle())
            DynamicLayoutFrm.TAG -> frm = DynamicLayoutFrm.getInstance(Bundle())
            SharedPrefFrm.TAG -> frm = SharedPrefFrm.getInstance(Bundle())
            AnimationFrm.TAG -> frm = AnimationFrm.getInstance(Bundle())
            ShareDataFrm.TAG -> frm = ShareDataFrm.getInstance(Bundle())
            SpeechTextActivity.TAG -> activity?.let { SpeechTextActivity.newIntent(it) }
            RecyclerViewFrm.TAG -> frm = RecyclerViewFrm.getInstance(Bundle())
            ExpendableFrm.TAG -> frm = ExpendableFrm.getInstance(Bundle())
            ReceiverFrm.TAG -> frm = ReceiverFrm.getInstance(Bundle())
            ServiceFrm.TAG -> frm = ServiceFrm.getInstance(Bundle())
            EcommLoginFrm.TAG -> frm = EcommLoginFrm.getInstance(Bundle())
            TimerFrm.TAG -> frm = TimerFrm.getInstance(Bundle())
            BlinkScanFrm.TAG -> frm = BlinkScanFrm.getInstance(Bundle())
            NavBothSideDrawerActivity.TAG -> activity?.let { NavBothSideDrawerActivity.newIntent(it) }
            NavDrawerActivity.TAG -> activity?.let { NavDrawerActivity.newIntent(it) }
            CoroutineFrm.TAG -> frm = CoroutineFrm.getInstance(Bundle())
            SplashActivity.TAG -> activity?.let { SplashActivity.newIntent(it) }
            DeviceInfoActivity.TAG -> activity?.let { DeviceInfoActivity.newIntent(it) }
            BarcodeFrm.TAG -> frm = BarcodeFrm.getInstance(Bundle())
            BottomNavigationFrm.TAG -> frm = BottomNavigationFrm.getInstance(Bundle())
            BottomSheetFrm.TAG -> frm = BottomSheetFrm.getInstance(Bundle())
            BottomSheetBehavFrm.TAG -> frm = BottomSheetBehavFrm.getInstance(Bundle())
            CollapseExpendFrm.TAG -> frm = CollapseExpendFrm.getInstance(Bundle())
            DigitalSignatureFrm.TAG -> frm = DigitalSignatureFrm.getInstance(Bundle())
            FabButtonFrm.TAG -> frm = FabButtonFrm.getInstance(Bundle())
            ViewPagerTabFrm.TAG -> frm = ViewPagerTabFrm.getInstance(Bundle())
            ContactFrm.TAG -> frm = ContactFrm.getInstance(Bundle())
            SmsFrm.TAG -> frm = SmsFrm.getInstance(Bundle())
            PhoneImageFrm.TAG -> frm = PhoneImageFrm.getInstance(Bundle())
            ZoomImageFrm.TAG -> frm = ZoomImageFrm.getInstance("", false)
            NotificationFrm.TAG -> frm = NotificationFrm.getInstance(Bundle())
            ExoPlayerFrm.TAG -> activity?.let { ExoPlayerActivity.newIntent(it, ExoPlayerFrm.TAG) }
            ExoPlayerRecyclerFrm.TAG -> activity?.let {
                ExoPlayerActivity.newIntent(
                    it,
                    ExoPlayerRecyclerFrm.TAG
                )
            }
            ExoPlayerPIPActivity.TAG -> activity?.let {
                ExoPlayerPIPActivity.newIntent(
                    it,
                    ExoPlayerFrm.TAG
                )
            }
            CollapseToolbarFrm.TAG -> frm = CollapseToolbarFrm.getInstance(Bundle())
            CollapseToolbarBehavFrm.TAG -> frm = CollapseToolbarBehavFrm.getInstance(Bundle())
            LocationHelperFrm.TAG -> frm = LocationHelperFrm.getInstance(Bundle())
            FcmFoodActivity.TAG -> activity?.let { FcmFoodActivity.newIntent(it) }
            WorkManagerFrm.TAG -> frm = WorkManagerFrm.getInstance(Bundle())
            LandingPageFrm.TAG -> frm = LandingPageFrm.getInstance(Bundle())
            CallLogFrm.TAG -> frm = CallLogFrm.getInstance(Bundle())
            CarAnimFrm.TAG -> activity?.let { MapActivity.newIntent(it, CarAnimFrm.TAG) }
            else -> frm = WidgetFrm.getInstance(Bundle())
        }
        frm?.let { navigateAddFragment(R.id.container, it, true) }
    }

    //  Tooltip -> https://github.com/skydoves/Balloon/blob/master/app/src/main/java/com/skydoves/balloondemo/BalloonUtils.kt

    override fun onPermissionGranted(mCustomPermission: List<String>) {
        super.onPermissionGranted(mCustomPermission)
        GlobalUtility.captureScreen(mActivity)
        activity?.showToast("Screen capture successfully")
    }

    private fun sortList(@SortListType.SortType sortType: Int) {
        when (sortType) {
            SortListType.DEFAULT -> mTaskList = ArrayList(listOf(*worktask))
            SortListType.ASCENDING -> mTaskList?.sort()
            SortListType.DESCENDING -> Collections.sort(mTaskList, Collections.reverseOrder())
        }
        mTaskList?.let { mHomeAdapter.notifyAdapter(it) }
    }

    private fun showPopupMenu(view: View) { // inflate menu
        val popup = PopupMenu(mActivity, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_sort, popup.menu)
        popup.setOnMenuItemClickListener(MyMenuItemClickListen(this))
        popup.show()
    }

    private class MyMenuItemClickListen(private var taskFrm: TaskFrm) :
        PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.menu_default -> taskFrm.sortList(SortListType.DEFAULT)
                R.id.menu_ascending -> taskFrm.sortList(SortListType.ASCENDING)
                R.id.menu_descending -> taskFrm.sortList(SortListType.DESCENDING)
            }
            return false
        }
    }

    private fun setUpFingrePrint() {
        val newExecutor: Executor =
            Executors.newSingleThreadExecutor()
        val myBiometricPrompt =
            BiometricPrompt(
                mActivity,
                newExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        activity?.runOnUiThread {
                            if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON)
                                GlobalUtility.showToast(errString.toString())
                            else GlobalUtility.showToast("You try too many time.\nAn unrecoverable error occurred")
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        activity?.runOnUiThread {
                            GlobalUtility.showToast("Fingerprint recognised successfully")
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        activity?.runOnUiThread {
                            GlobalUtility.showToast("Fingerprint not recognised")
                        }
                    }
                })

        val promptInfo = PromptInfo.Builder()
            .setTitle(getString(R.string.app_name))
            .setSubtitle("Subtitle goes here")
            .setDescription(getString(R.string.dummyText))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
        myBiometricPrompt.authenticate(promptInfo)
    }

    private fun startJobDispatcher() {
        ScheduledJobService.scheduleJob(activity)
        GlobalUtility.showToast(getString(R.string.job_schedule_successfully))
    }

    private fun appDarkTheme() {
        val mode =
            if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_NO
            ) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        // Change UI Mode
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
