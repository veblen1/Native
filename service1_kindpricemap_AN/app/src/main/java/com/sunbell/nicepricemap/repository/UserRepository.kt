package com.sunbell.nicepricemap.repository

import com.sunbell.nicepricemap.room.Restaurant
import com.sunbell.nicepricemap.room.RestaurantDao
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val dao: RestaurantDao
) : BaseRepository() {

    // 현재 위치 기반으로 가까운 상위 3개 식당 찾기
    fun findTop3NearbyRestaurants(latitude: Double, longitude: Double): List<Restaurant> {
        return dao.findTop3NearbyRestaurants(latitude, longitude)
    }

    // 식당 방문 상태 업데이트
    fun updateVisitStatus(restaurantId: Int, visit: Boolean) {
        dao.updateVisitStatus(restaurantId, visit)
    }

}