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
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.fillMaxSize


object CounterWidget: GlanceAppWidget() {
    // widget body
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // use provideContent, you can use composable function
        provideContent {
            Text(
                text = "0",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 26.sp
                )
            )
        }
    }
}