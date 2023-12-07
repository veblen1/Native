package com.sunbell.nicepricemap.view.util

import com.sunbell.nicepricemap.room.Restaurant
import java.io.InputStream


fun parseCsvFile(inputStream: InputStream): List<Restaurant> {
    val restaurants = mutableListOf<Restaurant>()
    inputStream.bufferedReader().useLines { lines ->
        lines.drop(1).forEach { line ->
            val tokens = line.split(",").map { it.trim() }  // 공백 제거
            val restaurant = Restaurant(
                region = tokens.getOrNull(0) ?: "",
                district = tokens.getOrNull(1) ?: "",
                category = tokens.getOrNull(2) ?: "",
                name = tokens.getOrNull(3) ?: "",
                contact = tokens.getOrNull(4) ?: "",
                address = tokens.getOrNull(5) ?: "",
                latitude = tokens.getOrNull(6)?.toDoubleOrNull() ?: 0.0,
                longitude = tokens.getOrNull(7)?.toDoubleOrNull() ?: 0.0,
                menu1 = tokens.getOrNull(8) ?: "",
                price1 = tokens.getOrNull(9)?.toIntOrNull() ?: 0,
                menu2 = tokens.getOrNull(10) ?: "",
                price2 = tokens.getOrNull(11)?.toIntOrNull() ?: 0,
                menu3 = tokens.getOrNull(12) ?: "",
                price3 = tokens.getOrNull(13)?.toIntOrNull() ?: 0,
                visit = tokens.getOrNull(14)?.toBoolean() ?: false
            )
            restaurants.add(restaurant)
        }
    }
    return restaurants
}