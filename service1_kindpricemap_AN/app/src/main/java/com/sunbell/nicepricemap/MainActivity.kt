package com.sunbell.nicepricemap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sunbell.nicepricemap.sharedpreferences.SharedPreferencesUtil
import com.sunbell.nicepricemap.view.AlarmPage
import com.sunbell.nicepricemap.view.BottomBar
import com.sunbell.nicepricemap.view.HomePage
import com.sunbell.nicepricemap.view.MapPage
import com.sunbell.nicepricemap.view.PermissionPage
import com.sunbell.nicepricemap.view.SettingPage
import com.sunbell.nicepricemap.view.TopBar
import com.sunbell.nicepricemap.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val sharedPreferencesUtil by lazy { SharedPreferencesUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController() // 초기화

            // 권한 관련 상태 관리 변수들
            var locationPermissionsGranted by remember { mutableStateOf(areLocationPermissionsAlreadyGranted()) }
            var shouldShowPermissionRationale by remember {
                mutableStateOf(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
            }

            var shouldDirectUserToApplicationSettings by remember { mutableStateOf(false) }
            var currentPermissionsStatus by remember {
                mutableStateOf(decideCurrentPermissionStatus(locationPermissionsGranted))
            }

            // 포그라운드 위치 권한 요청 런처
            val foregroundLocationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    locationPermissionsGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    shouldShowPermissionRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    shouldDirectUserToApplicationSettings = !shouldShowPermissionRationale && !locationPermissionsGranted
                    currentPermissionsStatus = decideCurrentPermissionStatus(locationPermissionsGranted)
                }
            )

            LaunchedEffect(key1 = true) {
                if (!locationPermissionsGranted) {
                    foregroundLocationPermissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            }

            // 앱의 나머지 UI 및 로직
            AppNavigation(navController, sharedPreferencesUtil)
        }
    }

    private fun areLocationPermissionsAlreadyGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun decideCurrentPermissionStatus(locationPermissionsGranted: Boolean): String {
        return if (locationPermissionsGranted) "Permissions Granted" else "Permissions Denied"
    }

}

@Composable
fun AppNavigation(
    navController: NavHostController,
    sharedPreferencesUtil: SharedPreferencesUtil
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current


    /**권한 목록 + 확인**/
    val permissionsList = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val hasAllPermissions = permissionsList.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    // 시작화면
    val startDestination = if (hasAllPermissions) {
        "Home"
    } else {
        "PermissionPage"
    }
    /************************/


    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {

        val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
        val showNavBar = remember(currentDestination) {
            currentDestination != "PermissionPage"
        }
        Box {
            Column {
                if (showNavBar) {
                    TopBar()
                }
                Box(modifier = Modifier.weight(1f)) {
                    NavHost(navController, startDestination = startDestination) {
                        composable("PermissionPage") { PermissionPage(navController) }

                        composable("Home") { HomePage(navController) }
                        composable("MapPage") { MapPage(navController) }
                        composable("AlarmPage") { AlarmPage(navController) }
                        composable("SettingPage") { SettingPage(navController, sharedPreferencesUtil) }
                    }
                }
                if (showNavBar) {
                    BottomBar(navController)
                }
            }
        }
    }
}