package com.teamenergy.ui.base.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.teamenergy.proxy.network.masterData.LoginDto
import com.teamenergy.proxy.network.masterData.UserDto

class SharedPreferenceUtils {
    companion object {
        private const val PREF_UTIL = "pref_util"
        private const val USER_DATA = "user_data"
        private const val LENGTH = "#LENGTH"
    }

    fun setUserData(activity: Activity, loginDto: LoginDto) {
        val sharedPref = activity.getSharedPreferences(PREF_UTIL, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(USER_DATA, Gson().toJson(loginDto))
        editor.apply()
    }

    fun getUserData(activity: Activity): LoginDto? {
        val sharedPref = activity.getSharedPreferences(PREF_UTIL, Context.MODE_PRIVATE)
        val user = sharedPref.getString(USER_DATA, "")
        return Gson().fromJson(user, LoginDto::class.java)
    }

    fun removeUserData(activity: Activity) {
        val sharedPref = activity.getSharedPreferences(PREF_UTIL,Context.MODE_PRIVATE)
        sharedPref.edit().remove(USER_DATA).commit()
    }
}