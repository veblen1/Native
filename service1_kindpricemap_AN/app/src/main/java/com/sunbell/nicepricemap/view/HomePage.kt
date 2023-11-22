package com.sunbell.nicepricemap.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import com.sunbell.nicepricemap.view.util.MapViewUtil
import com.sunbell.nicepricemap.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun HomePage(
    navController: NavController,
    userViewModel: UserViewModel,
    sharedPreferencesUtil: SharedPreferencesUtil
){
    // 현재 위치
    val currentLocation by userViewModel.currentLocation.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        MapViewUtil(height = 200.dp,
            userViewModel = userViewModel,
            navController = navController,
            sharedPreferencesUtil = sharedPreferencesUtil
        )
    }

}