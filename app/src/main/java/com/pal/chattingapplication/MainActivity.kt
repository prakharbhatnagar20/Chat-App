package com.pal.chattingapplication

import android.content.Context
import android.Manifest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pal.chattingapplication.screens.ChatListScreen
import com.pal.chattingapplication.screens.LoginScreen
import com.pal.chattingapplication.screens.ProfileScreen
import com.pal.chattingapplication.screens.SignUpScreen
import com.pal.chattingapplication.screens.SingleChatScreen
import com.pal.chattingapplication.ui.theme.ChattingApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route: String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{ChatId}"){
        fun createRoute(id: String) = "singleChat/$id"
    }
    object StatusList : DestinationScreen("statusList")
    object SingleStatus : DestinationScreen("singleStatus/{StatusId}"){
        fun createRoute(userId: String) = "singleStatus/$userId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChattingApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var vm = hiltViewModel<LCViewModel>()
                    MyApp(vm)

                }
            }
        }
    }
    @Composable
    fun MyApp(vm : LCViewModel){
        val context = LocalContext.current
        val locationUtils  = LocationUtils(context)
        LocationDisplay(locationUtils = locationUtils, vm, context = context)

    }

    @Composable
    fun LocationDisplay(locationUtils: LocationUtils, viewModel: LCViewModel, context: Context) {
        val location = viewModel.location.value

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                ) {
                    // I HAVE ACCESS to location
                    locationUtils.requestLocationUpdates(viewModel = viewModel)
                } else {
                    val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                    if (rationaleRequired) {
                        Toast.makeText(
                            context,
                            "Location Permission is required for this feature to work",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Location Permission is required. Please enable it in the Android Settings",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )

        LaunchedEffect(location) {
            if (location == null) {
                if (locationUtils.hasLocationPermission(context)) {
                    // Permission already granted update the location
                    locationUtils.requestLocationUpdates(viewModel)
                } else {
                    // Request location permission
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            } else {
                // Send location data when location is updated
//                val locationAndroid = Location("").apply {
//                    latitude = location.latitude
//                    longitude = location.longitude
//                }
//                viewModel.sendLocationData(locationAndroid)
            }
        }

//    DisposableEffect(Unit) {
//        onDispose {
//            locationUtils.stopLocationUpdates(viewModel)
//        }
//    }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ChatAppNavigation()
        }

    }

    @Composable
    fun ChatAppNavigation(){
        val navController = rememberNavController()
        var vm = hiltViewModel<LCViewModel>()
        NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route) {
            composable(DestinationScreen.SignUp.route){
                SignUpScreen(navController, vm)
            }
            composable(DestinationScreen.Login.route){
                LoginScreen(vm, navController = navController)
            }
            composable(DestinationScreen.ChatList.route){
                ChatListScreen(navController, vm)
            }
            composable(DestinationScreen.SingleChat.route){
               val chatId = it.arguments?.getString("ChatId")
                chatId?.let {
                    SingleChatScreen(navController = navController, vm = vm, chatId)
                }
            }

//            composable(DestinationScreen.StatusList.route){
//                StatusList(navController,vm)
//            }
            composable(DestinationScreen.Profile.route){
                ProfileScreen(navController, vm)
            }

            
        }
    }
}

