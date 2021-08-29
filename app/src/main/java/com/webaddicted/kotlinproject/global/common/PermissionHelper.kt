package com.webaddicted.kotlinproject.global.common

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.webaddicted.kotlinproject.R
import java.util.*

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */
class PermissionHelper {
    companion object {
        private const val PERMISSION_CODE = 1212
        //        private var mActivity: Activity? = null
        private var mCustomPermission: List<String>? = null
        private var mPerpermissionListener: PermissionListener? = null

        /**
         * Check if version is marshmallow and above.
         * Used in deciding to ask runtime permission
         * check single permission
         *
         * @param permissionListener is describe permission status
         * @param permissions        is single permission
         */
        fun requestSinglePermission(activity: Activity, @NonNull permissions: String, @NonNull permissionListener: PermissionListener) {
            mPerpermissionListener = permissionListener
            mCustomPermission = listOf(permissions)
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(activity, permissions) !== PackageManager.PERMISSION_GRANTED) {
                    //                askRequestPermissions(new String[]{permissions});
                    ActivityCompat.requestPermissions(activity, arrayOf(permissions), PERMISSION_CODE)
                    return
                }
            }
            permissionListener.onPermissionGranted(listOf(permissions))
        }

        /**
         * Check if version is marshmallow and above.
         * Used in deciding to ask runtime permission
         * NO of permission check a at time.
         *
         * @param permissionListener is describe permission status
         * @param permissions        is bundle of all permission
         */
        fun requestMultiplePermission(activity: Activity, @NonNull permissions: List<String>, @NonNull permissionListener: PermissionListener) {
            mPerpermissionListener = permissionListener
            mCustomPermission = permissions
            if (Build.VERSION.SDK_INT >= 23) {
                val listPermissionsAssign = ArrayList<String>()
                for (per in permissions) {
                    if (ContextCompat.checkSelfPermission(activity, per) !== PackageManager.PERMISSION_GRANTED)
                        listPermissionsAssign.add(per)
                }
                if (listPermissionsAssign.isNotEmpty()) {
                    ActivityCompat.requestPermissions(
                        activity,
                        listPermissionsAssign.toTypedArray(),
                        PERMISSION_CODE
                    )
                    return
                }
            }
            permissionListener.onPermissionGranted(permissions)
        }

        fun onRequestPermissionsResult(activity: Activity, @NonNull requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
            when (requestCode) {
                PERMISSION_CODE -> {
                    val listPermissionsNeeded =
                        mCustomPermission
                    val perms = HashMap<String, Int>()
                    if (listPermissionsNeeded != null) {
                        for (permission in listPermissionsNeeded) {
                            perms[permission] = PackageManager.PERMISSION_GRANTED
                        }
                    }
                    if (grantResults.isNotEmpty()) {
                        for (i in permissions.indices)
                            perms[permissions[i]] = grantResults[i]
                        var isAllGranted = true
                        if (listPermissionsNeeded != null) {
                            for (permission in listPermissionsNeeded) {
                                if (perms[permission] == PackageManager.PERMISSION_DENIED) {
                                    isAllGranted = false
                                    break
                                }
                            }
                        }
                        if (isAllGranted) {
                            mCustomPermission?.let { mPerpermissionListener?.onPermissionGranted(it) }
                        } else {
                            var shouldRequest = false
                            if (listPermissionsNeeded != null) {
                                for (permission in listPermissionsNeeded) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                                        shouldRequest = true
                                        break
                                    }
                                }
                            }
                            if (shouldRequest) {
                                ifCancelledAndCanRequest(activity)
                            } else {
                                //permission is denied (and never ask again is  checked)
                                //shouldShowRequestPermissionRationale will return false
                                ifCancelledAndCannotRequest(activity)
                            }
                        }
                    }
                }
            }
        }

        /**
         * permission cancel dialog
         */
        private fun ifCancelledAndCanRequest(activity: Activity) {
            activity.let {
                showDialogOK(
                    it, "Permission required for this app, please grant all permission .",
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> mCustomPermission?.let { it1 ->
                                mPerpermissionListener?.let { it2 ->
                                    requestMultiplePermission(
                                        activity,
                                        it1,
                                        it2
                                    )
                                }
                            }
                            DialogInterface.BUTTON_NEGATIVE -> mCustomPermission?.let { it1 ->
                                mPerpermissionListener?.onPermissionDenied(
                                    it1
                                )
                            }
                        }// proceed with logic by disabling the related features or quit the app.
                    })
            }
        }

        /**
         * forcefully stoped all permission dialog
         */
        private fun ifCancelledAndCannotRequest(activity: Activity) {
            showDialogOK(
                activity,
                activity.resources.getString(
                    R.string.forcefully_enable_permission
                ),
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                activity.packageName,
                                null
                            )
                            intent.data = uri
                            activity.startActivity(
                                intent
                            )
                        }
                        DialogInterface.BUTTON_NEGATIVE -> mCustomPermission?.let { it1 ->
                            mPerpermissionListener?.onPermissionDenied(
                                it1
                            )
                        }
                    }// proceed with logic by disabling the related features or quit the app.
                })
        }

        private fun showDialogOK(activity: Activity, message: String, okListener: DialogInterface.OnClickListener) {
            AlertDialog.Builder(activity).setMessage(message).setPositiveButton(
                activity.resources.getString(R.string.ok),
                okListener
            ).setNegativeButton(activity.resources.getString(R.string.cancel), okListener).create().show()
        }

        /**
         * Check multiple permission if version is marshmallow and above.
         * result in form of boolean
         *
         * @param permissions is bundle of all permission
         */
        fun checkMultiplePermission(activity: Activity, @NonNull permissions: List<String>): Boolean {
            mCustomPermission = permissions
            if (Build.VERSION.SDK_INT >= 23) {
                val listPermissionsAssign = ArrayList<String>()
                for (per in permissions) {
                    if (ContextCompat.checkSelfPermission(activity, per) !== PackageManager.PERMISSION_GRANTED) {
                        listPermissionsAssign.add(per)
                    }
                }
                if (listPermissionsAssign.isNotEmpty()) {
                    return false
                }
            }
            return true
        }

        /**
         * Check if version is marshmallow and above.
         * Used in deciding to ask runtime permission
         * check single permission
         *
         * @param permissions is single permission
         */
        fun checkPermission(activity: Activity, @NonNull permissions: String): Boolean {
            mCustomPermission = listOf(permissions)
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(activity, permissions) !== PackageManager.PERMISSION_GRANTED)
                    return false
            }
            return true
        }

        interface PermissionListener {
            fun onPermissionGranted(mCustomPermission: List<String>)

            fun onPermissionDenied(mCustomPermission: List<String>)

        }

        fun clearPermission() {
//            mActivity = null
            mCustomPermission = null
            mPerpermissionListener = null
        }
    }
}
