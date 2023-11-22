package com.sunbell.nicepricemap.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesUtil @Inject constructor(private val context: Context) {

    companion object {
        private const val PREF_NAME = "my_pref"
        private const val USER_ALARM = "false"
        // 음식점만보기 or 그외에도 보기
        //
    }

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // 사용자의 알람 설정 저장
    fun setUserAlarm(onOff: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(USER_ALARM, onOff)
            apply()
        }
    }

    // 사용자의 알람 설정 불러오기
    fun getUserAlarm(): Boolean? {
        return sharedPreferences.getBoolean(USER_ALARM, false)
    }
}