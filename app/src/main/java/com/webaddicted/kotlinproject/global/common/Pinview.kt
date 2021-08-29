package com.webaddicted.kotlinproject.global.common

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType.*
import android.text.TextWatcher
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.webaddicted.kotlinproject.R
import java.util.*

/**
 * Created by Deepak Sharma(webaddicted) on 14-09-2019.
 */
class Pinview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr), TextWatcher, View.OnFocusChangeListener,
    View.OnKeyListener {
    private val DENSITY = getContext().resources.displayMetrics.density
    /**
     * Attributes
     */
    private var mPinLength = 6
    private val editTextList = ArrayList<EditText>()
    private var mPinWidth = 38
    private var mTextSize = 18
    private var mPinHeight = 38
    private var mSplitWidth = 9
    private var mCursorVisible = false
    private var mDelPressed = false
    @DrawableRes
    @get:DrawableRes
    var pinBackground = R.drawable.otp_sample_background
        private set
    private var mPassword = false
    private var mHint: String? = ""
    private var inputType = InputType.NUMBER
    private var finalNumberPin = false
    private var mListener: PinViewEventListener? = null
    private var fromSetValue = false
    private var mForceKeyboard = true

    internal var mClickListener: View.OnClickListener? = null

    internal var currentFocus: View? = null

    internal var filters = arrayOfNulls<InputFilter>(1)
    internal lateinit var params: LinearLayout.LayoutParams

    private val keyboardInputType: Int
        get() {
            val it: Int
            when (inputType) {
                InputType.NUMBER -> it = TYPE_CLASS_NUMBER or TYPE_NUMBER_VARIATION_PASSWORD
                InputType.TEXT -> it = TYPE_CLASS_TEXT
                else -> it = TYPE_CLASS_TEXT
            }
            return it
        }

    /**
     * Returns the value of the Pinview
     *
     * @return
     */

    /**
     * Sets the value of the Pinview
     *
     * @param value
     */
    // Allow empty string to clear the fields
    var value: String
        get() {
            val sb = StringBuilder()
            for (et in editTextList) {
                sb.append(et.text.toString())
            }
            return sb.toString()
        }
        set(@NonNull value) {
            val regex = "[0-9]*"
            fromSetValue = true
            if (inputType == InputType.NUMBER && !value.matches(regex.toRegex()))
                return
            var lastTagHavingValue = -1
            for (i in editTextList.indices) {
                if (value.length > i) {
                    lastTagHavingValue = i
                    editTextList[i].setText(value[i].toString())
                } else {
                    editTextList[i].setText("")
                }
            }
            if (mPinLength > 0) {
                if (lastTagHavingValue < mPinLength - 1) {
                    currentFocus = editTextList[lastTagHavingValue + 1]
                } else {
                    currentFocus = editTextList[mPinLength - 1]
                    if (inputType == InputType.NUMBER || mPassword)
                        finalNumberPin = true
                    if (mListener != null)
                        mListener!!.onDataEntered(this, false)
                }
                currentFocus!!.requestFocus()
            }
            fromSetValue = false
            updateEnabledState()
        }

    /**
     * Getters and Setters
     */
    private val indexOfCurrentFocus: Int
        get() = editTextList.indexOf(currentFocus)


    var splitWidth: Int
        get() = mSplitWidth
        set(splitWidth) {
            this.mSplitWidth = splitWidth
            val margin = splitWidth / 2
            params.setMargins(margin, margin, margin, margin)

            for (editText in editTextList) {
                editText.layoutParams = params
            }
        }

    var pinHeight: Int
        get() = mPinHeight
        set(pinHeight) {
            this.mPinHeight = pinHeight
            params.height = pinHeight
            for (editText in editTextList) {
                editText.layoutParams = params
            }
        }

    var pinWidth: Int
        get() = mPinWidth
        set(pinWidth) {
            this.mPinWidth = pinWidth
            params.width = pinWidth
            for (editText in editTextList) {
                editText.layoutParams = params
            }
        }

    var pinLength: Int
        get() = mPinLength
        set(pinLength) {
            this.mPinLength = pinLength
            createEditTexts()
        }

    var isPassword: Boolean
        get() = mPassword
        set(password) {
            this.mPassword = password
            setTransformation()
        }

    var hint: String?
        get() = mHint
        set(mHint) {
            this.mHint = mHint
            for (editText in editTextList)
                editText.hint = mHint
        }

    enum class InputType {
        TEXT, NUMBER
    }

    /**
     * Interface for onDataEntered event.
     */

    interface PinViewEventListener {
        fun onDataEntered(pinview: Pinview, fromUser: Boolean)
    }

    init {
        gravity = Gravity.CENTER
        init(context, attrs, defStyleAttr)
    }

    /**
     * A method to take care of all the initialisations.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        this.removeAllViews()
        mPinHeight *= DENSITY.toInt()
        mPinWidth *= DENSITY.toInt()
        mSplitWidth *= DENSITY.toInt()
        setWillNotDraw(false)
        initAttributes(context, attrs, defStyleAttr)
        params = LinearLayout.LayoutParams(mPinWidth, mPinHeight)
        orientation = LinearLayout.HORIZONTAL
        createEditTexts()
        super.setOnClickListener {
            var focused = false
            for (editText in editTextList) {
                if (editText.length() == 0) {
                    editText.requestFocus()
                    openKeyboard()
                    focused = true
                    break
                }
            }
            if (!focused && editTextList.size > 0) { // Focus the last view
                editTextList[editTextList.size - 1].requestFocus()
            }
            if (mClickListener != null) {
                mClickListener!!.onClick(this@Pinview)
            }
        }
        // Bring up the keyboard
        val firstEditText = editTextList[0]
        firstEditText.postDelayed({ openKeyboard() }, 200)
        updateEnabledState()
    }

    /**
     * Creates editTexts and adds it to the pinview based on the pinLength specified.
     */

    private fun createEditTexts() {
        removeAllViews()
        editTextList.clear()
        var editText: EditText
        for (i in 0 until mPinLength) {
            editText = EditText(context)
            editText.textSize = mTextSize.toFloat()
            editTextList.add(i, editText)
            this.addView(editText)
            generateOneEditText(editText, "" + i)
        }
        setTransformation()
    }

    /**
     * This method gets the attribute values from the XML, if not found it takes the default values.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.Pinview, defStyleAttr, 0)
            pinBackground = array.getResourceId(R.styleable.Pinview_pinBackground, pinBackground)
            mPinLength = array.getInt(R.styleable.Pinview_pinLength, mPinLength)
            mPinHeight =
                array.getDimension(R.styleable.Pinview_pinHeight, mPinHeight.toFloat()).toInt()
            mPinWidth =
                array.getDimension(R.styleable.Pinview_pinWidth, mPinWidth.toFloat()).toInt()
            mSplitWidth =
                array.getDimension(R.styleable.Pinview_splitWidth, mSplitWidth.toFloat()).toInt()
            mTextSize =
                array.getDimension(R.styleable.Pinview_textSize, mTextSize.toFloat()).toInt()
            mCursorVisible = array.getBoolean(R.styleable.Pinview_cursorVisible, mCursorVisible)
            mPassword = array.getBoolean(R.styleable.Pinview_password, mPassword)
            mForceKeyboard = array.getBoolean(R.styleable.Pinview_forceKeyboard, mForceKeyboard)
            mHint = array.getString(R.styleable.Pinview_hint)
            val its = InputType.values()
            inputType = its[array.getInt(R.styleable.Pinview_inputType, 0)]
            array.recycle()
        }
    }

    /**
     * Takes care of styling the editText passed in the param.
     * tag is the index of the editText.
     *
     * @param styleEditText
     * @param tag
     */
    private fun generateOneEditText(styleEditText: EditText, tag: String) {
        params.setMargins(mSplitWidth / 2, mSplitWidth / 2, mSplitWidth / 2, mSplitWidth / 2)
        filters[0] = InputFilter.LengthFilter(1)
        styleEditText.filters = filters
        styleEditText.layoutParams = params
        styleEditText.gravity = Gravity.CENTER
        styleEditText.isCursorVisible = mCursorVisible

        if (!mCursorVisible) {
            styleEditText.isClickable = false
            styleEditText.hint = mHint

            styleEditText.setOnTouchListener { view, motionEvent ->
                // When back space is pressed it goes to delete mode and when u click on an edit Text it should get out of the delete mode
                mDelPressed = false
                false
            }
        }
        styleEditText.setBackgroundResource(pinBackground)
        styleEditText.setPadding(0, 0, 0, 0)
        styleEditText.tag = tag
        styleEditText.inputType = keyboardInputType
        styleEditText.addTextChangedListener(this)
        styleEditText.onFocusChangeListener = this
        styleEditText.setOnKeyListener(this)
    }

    /**
     * Requsets focus on current pin view and opens keyboard if forceKeyboard is enabled.
     *
     * @return the current focused pin view. It can be used to open softkeyboard manually.
     */
    fun requestPinEntryFocus(): View? {
        val currentTag = Math.max(0, indexOfCurrentFocus)
        val currentEditText = editTextList[currentTag]
        currentEditText.requestFocus()
        openKeyboard()
        return currentEditText
    }

    private fun openKeyboard() {
        if (mForceKeyboard) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    /**
     * Clears the values in the Pinview
     */
    fun clearValue() {
        value = ""
    }

    override fun onFocusChange(view: View, isFocused: Boolean) {
        if (isFocused && !mCursorVisible) {
            if (mDelPressed) {
                currentFocus = view
                mDelPressed = false
                return
            }
            for (editText in editTextList) {
                if (editText.length() == 0) {
                    if (editText !== view) {
                        editText.requestFocus()
                    } else {
                        currentFocus = view
                    }
                    return
                }
            }
            if (editTextList[editTextList.size - 1] !== view) {
                editTextList[editTextList.size - 1].requestFocus()
            } else {
                currentFocus = view
            }
        } else if (isFocused && mCursorVisible) {
            currentFocus = view
        } else {
            view.clearFocus()
        }
    }

    /**
     * Handles the character transformation for password inputs.
     */
    private fun setTransformation() {
        if (mPassword) {
            for (editText in editTextList) {
                editText.removeTextChangedListener(this)
                editText.transformationMethod = PinTransformationMethod()
                editText.addTextChangedListener(this)
            }
        } else {
            for (editText in editTextList) {
                editText.removeTextChangedListener(this)
                editText.transformationMethod = null
                editText.addTextChangedListener(this)
            }
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    /**
     * Fired when text changes in the editTexts.
     * Backspace is also identified here.
     *
     * @param charSequence
     * @param start
     * @param i1
     * @param count
     */
    override fun onTextChanged(charSequence: CharSequence, start: Int, i1: Int, count: Int) {
        if (charSequence.length == 1 && currentFocus != null) {
            val currentTag = indexOfCurrentFocus
            if (currentTag < mPinLength - 1) {
                var delay: Long = 1
                if (mPassword)
                    delay = 25
                this.postDelayed({
                    val nextEditText = editTextList[currentTag + 1]
                    nextEditText.isEnabled = true
                    nextEditText.requestFocus()
                }, delay)
            } else {
                //Last Pin box has been reached.
            }
            if (currentTag == mPinLength - 1 && inputType == InputType.NUMBER || currentTag == mPinLength - 1 && mPassword) {
                finalNumberPin = true
            }

        } else if (charSequence.length == 0) {
            val currentTag = indexOfCurrentFocus
            mDelPressed = true
            //For the last cell of the non password text fields. Clear the text without changing the focus.
            if (editTextList[currentTag].text.length > 0)
                editTextList[currentTag].setText("")
        }

        for (index in 0 until mPinLength) {
            if (editTextList[index].text.length < 1)
                break
            if (!fromSetValue && index + 1 == mPinLength && mListener != null)
                mListener!!.onDataEntered(this, true)
        }
        updateEnabledState()
    }

    /**
     * Disable views ahead of current focus, so a selector can change the drawing of those views.
     */
    private fun updateEnabledState() {
        val currentTag = Math.max(0, indexOfCurrentFocus)
        for (index in editTextList.indices) {
            val editText = editTextList[index]
            editText.isEnabled = index <= currentTag
        }
    }

    override fun afterTextChanged(editable: Editable) {

    }

    /**
     * Monitors keyEvent.
     *
     * @param view
     * @param i
     * @param keyEvent
     * @return
     */
    override fun onKey(view: View, i: Int, keyEvent: KeyEvent): Boolean {

        if (keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_DEL) {
            // Perform action on Del press
            val currentTag = indexOfCurrentFocus
            //Last tile of the number pad. Clear the edit text without changing the focus.
            if (inputType == InputType.NUMBER && currentTag == mPinLength - 1 && finalNumberPin || mPassword && currentTag == mPinLength - 1 && finalNumberPin) {
                if (editTextList[currentTag].length() > 0) {
                    editTextList[currentTag].setText("")
                }
                finalNumberPin = false
            } else if (currentTag > 0) {
                mDelPressed = true
                if (editTextList[currentTag].length() == 0) {
                    //Takes it back one tile
                    editTextList[currentTag - 1].requestFocus()
                    //Clears the tile it just got to
                    editTextList[currentTag].setText("")
                } else {
                    //If it has some content clear it first
                    editTextList[currentTag].setText("")
                }
            } else {
                //For the first cell
                if (editTextList[currentTag].text.length > 0)
                    editTextList[currentTag].setText("")
            }
            return true

        }

        return false
    }

    /**
     * A class to implement the transformation mechanism
     */
    private inner class PinTransformationMethod : TransformationMethod {

        private val BULLET = '\u2022'

        override fun getTransformation(source: CharSequence, view: View): CharSequence {
            return PasswordCharSequence(source)
        }

        override fun onFocusChanged(
            view: View,
            sourceText: CharSequence,
            focused: Boolean,
            direction: Int,
            previouslyFocusedRect: Rect) {}

        private inner class PasswordCharSequence(var source: CharSequence) :CharSequence {
            override val length: Int
                get() = source.length

            override fun get(index: Int): Char {
                return  BULLET
            }

            override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                return PasswordCharSequence(this.source.subSequence(startIndex, endIndex))
            }
        }
    }

    fun setPinBackgroundRes(@DrawableRes res: Int) {
        this.pinBackground = res
        for (editText in editTextList)
            editText.setBackgroundResource(res)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        mClickListener = l
    }

    fun getInputType(): InputType {
        return inputType
    }

    fun setInputType(inputType: InputType) {
        this.inputType = inputType
        val it = keyboardInputType
        for (editText in editTextList) {
            editText.inputType = it
        }
    }

    fun setPinViewEventListener(listener: PinViewEventListener) {
        this.mListener = listener
    }

    fun showCursor(status: Boolean) {
        mCursorVisible = status
        if (editTextList.isEmpty()) {
            return
        }
        for (edt in editTextList) {
            edt.isCursorVisible = status
        }
    }

    fun setTextSize(textSize: Int) {
        mTextSize = textSize
        if (editTextList.isEmpty()) {
            return
        }
        for (edt in editTextList) {
            edt.textSize = mTextSize.toFloat()
        }
    }

    fun setCursorColor(@ColorInt color: Int) {

        if (editTextList.isEmpty()) {
            return
        }
        for (edt in editTextList) {
            setCursorColor(edt, color)
        }
    }

    fun setTextColor(@ColorInt color: Int) {

        if (editTextList.isEmpty()) {
            return
        }
        for (edt in editTextList) {
            edt.setTextColor(color)
        }
    }

    fun setCursorShape(@DrawableRes shape: Int) {

        if (editTextList.isEmpty()) {
            return
        }
        for (edt in editTextList) {
            try {
                val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                f.isAccessible = true
                f.set(edt, shape)
            } catch (ignored: Exception) {
            }

        }
    }

    private fun setCursorColor(view: EditText, @ColorInt color: Int) {
        try {
            // Get the cursor resource id
            var field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            field.isAccessible = true
            val drawableResId = field.getInt(view)

            // Get the editor
            field = TextView::class.java.getDeclaredField("mEditor")
            field.isAccessible = true
            val editor = field.get(view)

            // Get the drawable and set a color filter
            val drawable = ContextCompat.getDrawable(view.context, drawableResId)
            if (drawable != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
            val drawables = drawable?.let { arrayOf<Drawable>(it, drawable) }

            // Set the drawables
            field = editor!!.javaClass.getDeclaredField("mCursorDrawable")
            field.isAccessible = true
            field.set(editor, drawables)
        } catch (ignored: Exception) {
        }

    }
}
