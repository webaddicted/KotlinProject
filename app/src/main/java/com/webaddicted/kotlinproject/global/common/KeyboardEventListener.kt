package com.webaddicted.kotlinproject.global.common

import android.view.ViewTreeObserver
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class KeyboardEventListener(
    private val activity: AppCompatActivity,
    private val callback: (isOpen: Boolean, height: Int) -> Unit
) : LifecycleObserver {

    private val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var lastState: Boolean = activity.isKeyboardOpen()

        override fun onGlobalLayout() {
            val isOpen = activity.isKeyboardOpen()
            if (isOpen == lastState) {
                return
            } else {
                dispatchKeyboardEvent(isOpen, activity.softkeybordHeight())
                lastState = isOpen
            }
        }
    }

    init {
        // Dispatch the current state of the keyboard
        dispatchKeyboardEvent(activity.isKeyboardOpen(), activity.softkeybordHeight())
        // Make the component lifecycle aware
        activity.lifecycle.addObserver(this)
        registerKeyboardListener()
    }

    private fun registerKeyboardListener() {
        activity.getRootView().viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    private fun dispatchKeyboardEvent(isOpen: Boolean, softkeybordHeight:Int) {
        when {
            isOpen  -> callback(true, softkeybordHeight)
            !isOpen -> callback(false, softkeybordHeight)
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    @CallSuper
    fun onLifecyclePause() {
        unregisterKeyboardListener()
    }

    private fun unregisterKeyboardListener() {
        activity.getRootView().viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}