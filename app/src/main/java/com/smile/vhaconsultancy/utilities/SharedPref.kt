package com.smile.vhaconsultancy.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPref private constructor(context: Context?) {
    var SharePrefKey: String? = "SharePrefKey"
    private var sharedpreferences: SharedPreferences? = null
    private var sharedpreferenceseditor: SharedPreferences.Editor? = null
    fun getSharedPref(key: String?): String? {
        return sharedpreferences?.getString(key, "")
    }
    fun getSharedPrefBool(key: String?): Boolean? {
        return sharedpreferences?.getBoolean(key, false)
    }
    fun putSharedPrefBool(key: String?, value: Boolean) {
        sharedpreferenceseditor?.putBoolean(key, value)
        sharedpreferenceseditor?.commit()
        sharedpreferenceseditor?.apply()
    }
    fun getSharedPrefFloat(key: String?): Float? {
        return sharedpreferences?.getFloat(key, 0.0f)
    }

    fun putSharedPrefString(key: String?, value: String?) {
        sharedpreferenceseditor?.putString(key, value)
        sharedpreferenceseditor?.commit()
        sharedpreferenceseditor?.apply()
    }

    fun putSharedPrefFloat(key: String?, value: Float) {
        sharedpreferenceseditor?.putFloat(key, value)
        sharedpreferenceseditor?.commit()
        sharedpreferenceseditor?.apply()
    }

    companion object {
        private var myObj: SharedPref? = null
        fun getInstance(context: Context?): SharedPref? {
            if (myObj == null) {
                myObj = SharedPref(context)
            }
            return myObj
        }
    }

    init {
        sharedpreferences = context?.getSharedPreferences(SharePrefKey, Context.MODE_PRIVATE)
        sharedpreferenceseditor = sharedpreferences?.edit()
    }
}