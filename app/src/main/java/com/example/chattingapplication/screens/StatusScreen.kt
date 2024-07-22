package com.example.chattingapplication.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.chattingapplication.LCViewModel

@Composable
fun StatusList(navController: NavController, vm: LCViewModel){
    BottomNavigationMenu(selectedItem = BottomNavigationMenu.STATUSLIST, navController = navController)
}