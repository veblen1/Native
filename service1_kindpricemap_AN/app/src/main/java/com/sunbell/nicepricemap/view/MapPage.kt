package com.sunbell.nicepricemap.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import com.sunbell.nicepricemap.view.util.MapViewUtil
import com.sunbell.nicepricemap.viewmodel.UserViewModel


@Composable
fun MapPage(
    navController: NavController,
    userViewModel: UserViewModel,
    sharedPreferencesUtil: SharedPreferencesUtil
){
    MapViewUtil(navController = navController,
        userViewModel = userViewModel,
        sharedPreferencesUtil = sharedPreferencesUtil
    )
}