package com.sunbell.nicepricemap.repository

import com.sunbell.nicepricemap.repository.BaseRepository
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : BaseRepository() {

}