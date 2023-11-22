package com.sunbell.nicepricemap.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionPage(navController: NavController){
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val permissionsList = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10, API level 29
        permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12, API level 33
        permissionsList.add(Manifest.permission.READ_MEDIA_IMAGES)
    }

    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = permissionsList
    )

    LaunchedEffect(Unit) {
        multiplePermissionState.launchMultiplePermissionRequest()

        if (multiplePermissionState.allPermissionsGranted) {
            navController.navigate("Home")
        } else if (multiplePermissionState.revokedPermissions.isNotEmpty()) {
            delay(5000)
            showDialog = true
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                (context as Activity).finish()  // 앱 종료
            },
            title = {
                Text(text = "권한 요청")
            },
            text = {
                Text("이 앱은 위치 권한이 필요합니다.\n설정에서 권한을 허용으로 해주세요.")
            },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("설정으로 가기")
                }
            },
            dismissButton = {
                Button(onClick = {
                    (context as Activity).finish()  // 앱 종료
                }) {
                    Text("취소")
                }
            }
        )
    }
}