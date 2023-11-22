package com.sunbell.nicepricemap.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.sunbell.nicepricemap.R
import com.sunbell.nicepricemap.ui.theme.logoColor

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .scale(0.9f)
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .height(72.dp)
            .zIndex(2f),
    ) {
        // 그림자 추가를 위한 Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .shadow(elevation = 1.dp, shape = RectangleShape)
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            BottomBarButton("홈") {
                navController.navigate("Home")
            }

            BottomBarButton("지도") {
                navController.navigate("MapPage")
            }

            BottomBarButton("설정") {
                navController.navigate("SettingPage")
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun BottomBarButton(label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .widthIn(min = 64.dp, max = 80.dp)
    ) {
        val icon = when (label) {
            "홈" -> Icons.Default.Home
            "지도" -> Icons.Default.LocationOn
            "설정" -> Icons.Default.Settings
            else -> Icons.Default.Home  // 기본값
        }

        Icon(
            imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp)) // 아이콘과 텍스트 사이 간격

        Text(label)
    }
}