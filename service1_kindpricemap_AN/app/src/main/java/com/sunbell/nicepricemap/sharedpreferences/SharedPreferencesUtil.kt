package com.sunbell.nicepricemap.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.naver.maps.geometry.LatLng
import javax.inject.Inject

class SharedPreferencesUtil @Inject constructor(private val context: Context) {

    companion object {
        private const val PREF_NAME = "my_pref"
        private const val USER_ALARM = "false"
        private const val LAST_LATITUDE = "37.541"
        private const val LAST_LONGITUDE = "126.986"
        // 음식점만보기 or 그외에도 보기
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

    // 마지막 위치 위도 저장
    fun setLastLatitude(latitude: String) {
        with(sharedPreferences.edit()) {
            putString(LAST_LATITUDE, latitude)
            apply()
        }
    }

    // 마지막 위치 위도 불러오기
    fun getLastLatitude(): String? {
        return sharedPreferences.getString(LAST_LATITUDE, LAST_LATITUDE)
    }

    // 마지막 위치 경도 저장
    fun setLastLongitude(longitude: String) {
        with(sharedPreferences.edit()) {
            putString(LAST_LONGITUDE, longitude)
            apply()
        }
    }

    // 마지막 위치 경도 불러오기
    fun getLastLongitude(): String? {
        return sharedPreferences.getString(LAST_LONGITUDE, LAST_LONGITUDE)
    }

}