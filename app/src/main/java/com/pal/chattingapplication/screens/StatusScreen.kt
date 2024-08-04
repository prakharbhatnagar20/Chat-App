package com.pal.chattingapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pal.chattingapplication.LCViewModel

@Composable
fun StatusList(navController: NavController, vm: LCViewModel){
    Box (modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp) // Add padding equal to the BottomNavigation's height
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No Status")


        }
//        BottomNavigationMenu(selectedItem = BottomNavigationMenu.STATUSLIST, navController = navController, modifier = Modifier.align(
//            Alignment.BottomCenter))

    }

}