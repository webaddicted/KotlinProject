package com.webaddicted.kotlinproject.global.sharedpref

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class PreferenceUtils {

    companion object {
        private val PREFS_NAME = "local_pref"
        private val GLOBAL_PREFS_NAME = "global_pref"
        private var mLocalPreferences: SharedPreferences? = null
        private var mGlobalPreferences: SharedPreferences? = null
        fun getInstance(context: Context) {
            mLocalPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            mGlobalPreferences = context.getSharedPreferences(GLOBAL_PREFS_NAME, Context.MODE_PRIVATE)
        }
    }
//    {START LOCAL PREFERENCE  SAVE}

    /**
     * Get data from mPreferenceUtil with key {key} & of type {obj}
     *
     * @param key          preference key
     * @param defautlValue default key for preference
     * @param <T>
     * @return
    </T> */
    fun <T> getPreference(key: String, defautlValue: T): T? {
        try {
            if (defautlValue is String) {
                return mLocalPreferences?.getString(key, defautlValue as String) as T
            } else if (defautlValue is Int) {
                return mLocalPreferences?.getInt(key, defautlValue as Int) as T
            } else if (defautlValue is Boolean) {
                return mLocalPreferences?.getBoolean(key, defautlValue as Boolean) as T
            } else if (defautlValue is Float) {
                return mLocalPreferences?.getFloat(key, defautlValue as Float) as T
            } else if (defautlValue is Long) {
                return mLocalPreferences?.getLong(key, defautlValue as Long) as T
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Save data to mPreferenceUtil with key {key} & of type {obj}
     *
     * @param key
     * @param value
     * @param <T>
     * @return
    </T> */
    fun <T> setPreference(key: String, value: T) {
        try {
            val editor = mLocalPreferences?.edit()
            if (value is String) {
                editor?.putString(key, value as String)
            } else if (value is Int) {
                editor?.putInt(key, value as Int)
            } else if (value is Boolean) {
                editor?.putBoolean(key, value as Boolean)
            } else if (value is Float) {
                editor?.putFloat(key, value as Float)
            } else if (value is Long) {
                editor?.putLong(key, value as Long)
            }
            editor?.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * clear key preference when required
     */
    fun removeKey(key: String) {
        if (mLocalPreferences != null)
            mLocalPreferences?.edit()?.remove(key)?.commit()
    }

    /**
     * clear preference when required
     */
    fun clearAllPreferences() {
        if (mLocalPreferences != null)
            mLocalPreferences?.edit()?.clear()?.commit()
    }

    /**
     * Clear all Preference accept keyToBeSaved
     *
     * @param keyToBeSaved
     */
    fun clearAllPreferences(keyToBeSaved: Array<String>) {
        if (mLocalPreferences != null) {
            val map = ConcurrentHashMap(mLocalPreferences?.all)
            for (stringObjectEntry in map.keys) {
                if (!Arrays.asList(*keyToBeSaved).contains(stringObjectEntry)) {
                    val editor = mLocalPreferences?.edit()
                    editor?.remove(stringObjectEntry)?.commit()
                }
            }
        }
    }
//    {END LOCAL PREFERENCE  SAVE}

//    {START LOCAL PREFERENCE  SAVE}

    /**
     * Get data from mPreferenceUtil with key {key} & of type {obj}
     *
     * @param key          preference key
     * @param defautlValue default key for preference
     * @param <T>
     * @return
    </T> */
    fun <T> getGlobalPreference(key: String, defautlValue: T): T? {
        try {
            if (defautlValue is String) {
                return mGlobalPreferences?.getString(key, defautlValue as String) as T
            } else if (defautlValue is Int) {
                return mGlobalPreferences?.getInt(key, defautlValue as Int) as T
            } else if (defautlValue is Boolean) {
                return mGlobalPreferences?.getBoolean(key, defautlValue as Boolean) as T
            } else if (defautlValue is Float) {
                return mGlobalPreferences?.getFloat(key, defautlValue as Float) as T
            } else if (defautlValue is Long) {
                return mGlobalPreferences?.getLong(key, defautlValue as Long) as T
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Save data to mPreferenceUtil with key {key} & of type {obj}
     *
     * @param key
     * @param value
     * @param <T>
     * @return
    </T> */
    fun <T> setGlobalPreference(key: String, value: T) {
        try {
            val editor = mGlobalPreferences?.edit()
            if (value is String) {
                editor?.putString(key, value as String)
            } else if (value is Int) {
                editor?.putInt(key, value as Int)
            } else if (value is Boolean) {
                editor?.putBoolean(key, value as Boolean)
            } else if (value is Float) {
                editor?.putFloat(key, value as Float)
            } else if (value is Long) {
                editor?.putLong(key, value as Long)
            }
            editor?.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * clear key preference when required
     */
    fun removeGlobalKey(key: String) {
        if (mGlobalPreferences != null)
            mGlobalPreferences?.edit()?.remove(key)?.commit()
    }

    /**
     * clear preference when required
     */
    fun clearAllGlobalPreferences() {
        if (mGlobalPreferences != null)
            mGlobalPreferences?.edit()?.clear()?.commit()
    }

    /**
     * Clear all Preference accept keyToBeSaved
     *
     * @param keyToBeSaved
     */
    fun clearAllGlobalPreferences(keyToBeSaved: Array<String>) {
        if (mGlobalPreferences != null) {
            val map = ConcurrentHashMap(mGlobalPreferences?.all)
            for (stringObjectEntry in map.keys) {
                if (!Arrays.asList(*keyToBeSaved).contains(stringObjectEntry)) {
                    val editor = mGlobalPreferences?.edit()
                    editor?.remove(stringObjectEntry)?.commit()
                }
            }
        }
    }
//    {END GLOBAL PREFERENCE  SAVE}

}