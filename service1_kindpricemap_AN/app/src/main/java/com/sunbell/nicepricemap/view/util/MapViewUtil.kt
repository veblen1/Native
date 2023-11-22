package com.sunbell.nicepricemap.view.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import com.sunbell.nicepricemap.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapViewUtil(
    height: Dp = Dp.Unspecified,
    navController: NavController,
    userViewModel: UserViewModel,
    sharedPreferencesUtil: SharedPreferencesUtil
) {
    // 현재 위치
    val currentLocation by userViewModel.currentLocation.collectAsState()

    var showInfo by remember { mutableStateOf(false) } // 대기화면
    var center = LatLng(37.541, 126.986)

    LaunchedEffect(true) {
        delay(500)
        showInfo = true // 0.5초 딜레이
    }

    /** 지도 기본설정 **/
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoom = 50.0,
                minZoom = 5.0,
                locationTrackingMode = LocationTrackingMode.Follow
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true)
        )
    }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(center, 17.0)
    }
    /*****************/


    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        if (showInfo) {
            NaverMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                locationSource = rememberFusedLocationSource()
            ) {
                // 위치들
            }
        }
        else{
            LoadingScreen()
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // 반투명 배경
            .clickable(enabled = false, onClick = {}), // 클릭 막기
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator() // 로딩 아이콘
    }
}