package com.sunbell.nicepricemap.view

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.sunbell.nicepricemap.room.Restaurant


object CounterWidget: GlanceAppWidget() {
    // widget body
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // use provideContent, you can use composable function
        provideContent {
            MyContent(restaurants = listOf(
                // 테스트 데이터 - 실제로는 ViewModel에서 제공받아야 함
                Restaurant(name = "식당1", category = "한식", menu1 = "김치찌개", price1 = 8000),
                Restaurant(name = "식당2", category = "중식", menu1 = "짜장면", price1 = 10000),
                Restaurant(name = "식당3", category = "일식", menu1 = "초밥", price1 = 12000)
            ))
        }
    }
}


@Composable
fun MyContent(restaurants: List<Restaurant>) {
    Column {
        restaurants.forEach { restaurant ->
            Text(
                text = "${restaurant.category} - ${restaurant.name}: ${restaurant.menu1} (${restaurant.price1}원)",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            )
        }
    }
}