package com.sunbell.nicepricemap.repository

import com.sunbell.nicepricemap.repository.BaseRepository
import com.sunbell.nicepricemap.room.RestaurantDao
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import com.sunbell.nicepricemap.view.util.parseCsvFile
import java.io.InputStream
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val dao: RestaurantDao
) : BaseRepository() {

}