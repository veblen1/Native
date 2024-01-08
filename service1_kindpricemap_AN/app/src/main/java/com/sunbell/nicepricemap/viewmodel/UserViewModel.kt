package com.sunbell.nicepricemap.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sunbell.nicepricemap.repository.UserRepository
import com.sunbell.nicepricemap.room.Restaurant
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import com.sunbell.nicepricemap.view.CounterWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : ViewModel() {

    // 가까운 식당
    private val _nearbyRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val nearbyRestaurants: StateFlow<List<Restaurant>> = _nearbyRestaurants

    // GPS
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                viewModelScope.launch {
                    _currentLocation.value = it
                    sharedPreferencesUtil.setLastLatitude(it.latitude.toString())
                    sharedPreferencesUtil.setLastLongitude(it.longitude.toString())
                }
            }
        }
    }

    // 현재 위치 기반으로 가까운 상위 3개 식당 찾기
    fun getTop3NearbyRestaurants() {
        viewModelScope.launch {
            _currentLocation.value?.let {
                val restaurants = userRepository.findTop3NearbyRestaurants(it.latitude, it.longitude)
                _nearbyRestaurants.value = restaurants
            }
        }
    }

    // 식당 방문 상태 업데이트
    fun updateRestaurantVisitStatus(restaurantId: Int, visit: Boolean) {
        viewModelScope.launch {
            userRepository.updateVisitStatus(restaurantId, visit)
        }
    }

    // Glance

    fun updateLocationAndRestaurants(location: Location) {
        viewModelScope.launch {
            _currentLocation.value = location
            sharedPreferencesUtil.setLastLatitude(location.latitude.toString())
            sharedPreferencesUtil.setLastLongitude(location.longitude.toString())

            val restaurants = userRepository.findTop3NearbyRestaurants(location.latitude, location.longitude)
            _nearbyRestaurants.value = restaurants

            // Glance 위젯 업데이트 로직
//            updateGlanceWidgetWithRestaurants(restaurants)
        }
    }
}