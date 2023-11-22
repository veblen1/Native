package com.sunbell.nicepricemap.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sunbell.nicepricemap.repository.UserRepository
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : ViewModel() {

}