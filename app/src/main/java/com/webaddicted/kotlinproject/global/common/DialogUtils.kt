package com.webaddicted.kotlinproject.global.common

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.view.interfac.AlertDialogListener
import com.webaddicted.kotlinproject.view.interfac.AlertRetryDialogListener


class DialogUtil {
    companion object {
        private val TAG = DialogUtil::class.qualifiedName
//    {START SHOW DIALOG STYLE}
//    apply on resume method

        /**
         * show dialog with transprant background
         *
         * @param activity reference of activity
         * @param dialog   reference of dialog
         */
        fun modifyDialogBounds(activity: Activity?, dialog: Dialog) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        activity!!,
                        android.R.color.transparent
                    )
                )
            )
            dialog.window?.decorView?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val lp = WindowManager.LayoutParams()
            val window = dialog.window
            lp.copyFrom(window!!.attributes)
            //This makes the dialog take up the full width
            //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.width = (dialog.context.resources.displayMetrics.widthPixels * 0.83).toInt()
            //  lp.height = (int) (dialog.getContext().getResources().getDisplayMetrics().heightPixels * 0.55);
            window.attributes = lp
        }

        /**
         * show dialog in full screen
         *
         * @param activity reference of activity
         * @param dialog   reference of dialog
         */
        fun fullScreenDialogBounds(activity: Activity, dialog: Dialog?) {
            if (dialog != null && dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            activity,
                            android.R.color.transparent
                        )
                    )
                )
                dialog.window?.decorView?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                dialog.window?.setLayout(width, height)
                val lp = WindowManager.LayoutParams()
                val window = dialog.window
                lp.copyFrom(window!!.attributes)
                //This makes the dialog take up the full width
                //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.width = (dialog.context.resources.displayMetrics.widthPixels * 0.83).toInt()
                //  lp.height = (int) (dialog.getContext().getResources().getDisplayMetrics().heightPixels * 0.55);
                window.attributes = lp
            }
        }

        /**
         * show dialog in full screen
         *
         * @param activity reference of activity
         * @param dialog   reference of dialog
         */
        fun fullScreenTransDialogBounds(activity: Activity, dialog: Dialog?) {
            if (dialog != null && dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            activity,
                            android.R.color.transparent
                        )
                    )
                )
                dialog.window?.decorView?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                dialog.window!!.setLayout(width, height)
                val lp = WindowManager.LayoutParams()
                val window = dialog.window
                lp.copyFrom(window!!.attributes)
                //This makes the dialog take up the full width
                //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                //            lp.width = (int) (dialog.getContext().getResources().getDisplayMetrics().widthPixels * 0.83);
                //  lp.height = (int) (dialog.getContext().getResources().getDisplayMetrics().heightPixels * 0.55);
                window.attributes = lp
            }
        }
        //    {STOP SHOW DIALOG STYLE}
        /**
         * This method is used to modify dialog bounds
         *
         * @param dialog
         */
        fun fullScreenDialogBound(dialog: Dialog) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        dialog.context,
                        android.R.color.transparent
                    )
                )
            )
            val lp = WindowManager.LayoutParams()
            val window = dialog.window
            lp.copyFrom(window?.attributes)
            //This makes the dialog take up the full width
            //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.width = dialog.context.resources.displayMetrics.widthPixels
            //  lp.height = (int) (dialog.getContext().getResources().getDisplayMetrics().heightPixels * 0.55);
            window?.attributes = lp
        }


        /**
         * createMessageAlert dialog
         *
         * @param title               return dialog title string value
         * @param message             return dialog messge
         * @param btnOk               return button name event
         * @param btnCancel           return button name event
         * @param btnRetry            return button click event
         * @param alertDialogListener click event listener
         */
        fun alertFunction(
            context: Context?,
            title: String,
            message: String?,
            btnOk: String,
            btnCancel: String,
            btnRetry: String,
            alertDialogListener: AlertRetryDialogListener
        ): AlertDialog {
            val dialogAnimation = R.style.DialogSlideUpAnimation
            val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)
            builder.setCancelable(false)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(btnOk) { dialog, which -> alertDialogListener.okClick() }
            builder.setNegativeButton(btnCancel) { dialog, which -> alertDialogListener.cancelClick() }
            builder.setNeutralButton(btnRetry) { dialogInterface, i -> alertDialogListener.okRetry() }
            val dialogs = builder.create()
            dialogs.window?.attributes?.windowAnimations = dialogAnimation
            dialogs.show()
            return dialogs
        }

        fun alertFunction(
            context: Context?,
            title: String,
            messgae: String,
            btnOk: String,
            btnCancel: String,
            alertDialogListener: AlertDialogListener
        ): AlertDialog {
            val dialogAnimation = R.style.DialogSlideUpAnimation
            val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)
            builder.setCancelable(false)
            builder.setTitle(title)
            builder.setMessage(messgae)
            builder.setPositiveButton(btnOk) { dialog, which -> alertDialogListener.okClick() }
            builder.setNegativeButton(btnCancel) { dialog, which -> alertDialogListener.cancelClick() }
            val dialogs = builder.create()
            dialogs.window?.attributes?.windowAnimations = dialogAnimation
            dialogs.show()
            return dialogs
        }

        fun createMessageAlert(
            context: Context?,
            title: String,
            message: String,
            btnOk: String,
            btnListener: DialogInterface.OnClickListener
        ): AlertDialog {
            val dialogAnimation = R.style.DialogSlideUpAnimation
            val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)
            builder.setCancelable(false)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(btnOk, btnListener)
            val dialogs = builder.create()
            dialogs.window?.attributes?.windowAnimations = dialogAnimation
            dialogs.show()
            return dialogs
        }

        /**
         * @param context    referance of activity
         * @param title      title of dialog
         * @param msg        message of dialog
         * @param okBtn      ok button text
         * @param okListener button click listener
         * @return dialog
         */
        fun showOkDialog(
            context: Context,
            title: String,
            msg: String,
            okBtn: String,
            okListener: DialogInterface.OnClickListener
        ): AlertDialog {
            return showOKDialog(
                context,
                R.style.AlertDialogStyle,
                R.style.DialogLeftRightAnimation,
                title,
                msg,
                0,
                true,
                okBtn,
                okListener
            )
        }

        /**
         * @param context         referance of activity
         * @param style           set dialog style
         * @param dialogAnimation set enter exit animation on dilaog
         * @param title           title of dialog
         * @param msg             message of dialog
         * @param icon            set icon
         * @param isCancelable    set is dialog cancelable
         * @param okBtn           ok button text
         * @param okListener      button click listener
         * @return dialog
         */
        fun showOKDialog(
            context: Context,
            style: Int,
            dialogAnimation: Int,
            title: String,
            msg: String,
            icon: Int,
            isCancelable: Boolean,
            okBtn: String,
            okListener: DialogInterface.OnClickListener
        ): AlertDialog {
            val alertDialog = AlertDialog.Builder(context, style).create()
            alertDialog.window?.attributes?.windowAnimations = dialogAnimation
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            if (icon > 0) alertDialog.setIcon(icon)
            alertDialog.setCancelable(isCancelable)
            alertDialog.setButton(0, okBtn, okListener)
            alertDialog.show()
            return alertDialog
        }

        /**
         * @param context        referance of activity
         * @param title          title of dialog
         * @param msg            message of dialog
         * @param okListener     ok button click listener
         * @param cancelListener cancel button click listener
         * @return dialog
         */
        fun showOkCancelDialog(
            context: Context,
            title: String,
            msg: String,
            okListener: DialogInterface.OnClickListener,
            cancelListener: DialogInterface.OnClickListener
        ): AlertDialog.Builder {
            return showOKCancelDialog(
                context,
                R.style.AlertDialogStyle,
                R.style.DialogSlideUpAnimation,
                title,
                msg,
                0,
                false,
                context.getString(R.string.ok),
                context.getString(R.string.cancel),
                okListener,
                cancelListener
            )
        }

        /**
         * @param context         referance of activity
         * @param style           set dialog style
         * @param dialogAnimation set enter exit animation on dilaog
         * @param title           title of dialog
         * @param msg             message of dialog
         * @param icon            set icon
         * @param isCancelable    set is dialog cancelable
         * @param okBtn           ok button text
         * @param cancelBtn       cancel button text
         * @param okListener      ok button click listener
         * @param cancelListener  cancel button click listener
         * @return dialog
         */
        fun showOKCancelDialog(
            context: Context,
            style: Int,
            dialogAnimation: Int,
            title: String,
            msg: String,
            icon: Int,
            isCancelable: Boolean,
            okBtn: String,
            cancelBtn: String,
            okListener: DialogInterface.OnClickListener,
            cancelListener: DialogInterface.OnClickListener
        ): AlertDialog.Builder {
            val alertDialog = AlertDialog.Builder(context, style)
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            if (icon > 0) alertDialog.setIcon(icon)
            alertDialog.setCancelable(isCancelable)
            alertDialog.setPositiveButton(okBtn, okListener)
            alertDialog.setNegativeButton(cancelBtn, cancelListener)
            val alertDialogs = alertDialog.create()
            alertDialogs.window!!.attributes.windowAnimations = dialogAnimation
            alertDialog.show()
            return alertDialog
        }

        /**
         * @param context        referance of activity
         * @param title          title of single selection dilaog
         * @param items          item list in dilog
         * @param okListener     ok click listener
         * @param cancelListener cancel click listener
         * @param <T>
         * @return dialog
        </T> */
        fun <T> getSingleChoiceDialog(
            context: Context,
            title: String,
            items: List<T>,
            okListener: DialogInterface.OnClickListener,
            cancelListener: DialogInterface.OnClickListener
        ): AlertDialog {
            return showSingleChoiceDialog(
                context,
                R.style.AlertDialogStyle,
                R.style.DialogSlideUpAnimation,
                title,
                items,
                0,
                context.resources.getString(R.string.ok),
                context.resources.getString(R.string.cancel),
                okListener,
                cancelListener
            )
        }

        /**
         * @param context         referance of activity
         * @param style           dialog style
         * @param dialogAnimation animation on dialog
         * @param title           title of dialog
         * @param items           list of dialog
         * @param checkedItem     initial selected item
         * @param okBtn           ok button text
         * @param cancelBtn       cancel button text
         * @param okListener      ok click listener
         * @param cancelListener  cancel click listener
         * @param <T>
         * @return dialog
        </T> */
        fun <T> showSingleChoiceDialog(
            context: Context,
            style: Int,
            dialogAnimation: Int,
            title: String?,
            items: List<T>,
            checkedItem: Int,
            okBtn: String,
            cancelBtn: String,
            okListener: DialogInterface.OnClickListener,
            cancelListener: DialogInterface.OnClickListener
        ): AlertDialog {
            val size = items.size
            val itemArray = arrayOfNulls<String>(size)
            for (i in 0 until size) {
                itemArray[i] = items[i].toString()
            }
            val builder = AlertDialog.Builder(context, style)
            if (title != null) builder.setTitle(title)
            builder.setSingleChoiceItems(itemArray, checkedItem) { dialog, which ->
                okListener.onClick(dialog, which)
            }
            builder.setPositiveButton(okBtn, okListener)
            builder.setNegativeButton(cancelBtn, cancelListener)
            val alertDialog = builder.create()
            alertDialog.window?.attributes?.windowAnimations = dialogAnimation
            alertDialog.show()
            return alertDialog
        }

        /**
         * @param context    referance of activity
         * @param title      title of dialog
         * @param items      list show in diloag
         * @param okListener ok click listener
         * @param <T>
         * @return dialog
        </T> */
        fun <T> showListDialog(
            context: Context,
            title: String,
            items: List<T>,
            okListener: DialogInterface.OnClickListener
        ): AlertDialog {
            return showListDialog(
                context,
                R.style.AlertDialogStyle,
                R.style.DialogSlideUpAnimation,
                title,
                items,
                okListener
            )
        }

        /**
         * @param context         referance of activity
         * @param style           dialog style
         * @param dialogAnimation dialog animation
         * @param title           dialog title
         * @param items           list show in dilaog
         * @param listener        ok click listener
         * @param <T>
         * @return dialog
        </T> */
        fun <T> showListDialog(
            context: Context,
            style: Int,
            dialogAnimation: Int,
            title: String?,
            items: List<T>,
            listener: DialogInterface.OnClickListener
        ): AlertDialog {
            val size = items.size
            val itemArray = arrayOfNulls<String>(size)
            for (i in 0 until size) {
                itemArray[i] = items[i].toString()
            }
            val builder = AlertDialog.Builder(context, style)
            if (title != null) builder.setTitle(title)
            builder.setItems(itemArray, listener)
            val alertDialog = builder.create()
            alertDialog.window!!.attributes.windowAnimations = dialogAnimation
            alertDialog.show()
            return alertDialog
        }
    }
}
