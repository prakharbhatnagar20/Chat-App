package com.pal.chattingapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pal.chattingapplication.R
import com.pal.chattingapplication.DestinationScreen
import com.pal.chattingapplication.navigateTo


enum class BottomNavigationMenu(val icon: Int, val label: String, val navDestination: DestinationScreen) {
    CHATLIST(R.drawable.message, "Chat", DestinationScreen.ChatList),
//    STATUSLIST(R.drawable.charging_circle, "Status", DestinationScreen.StatusList),
    PROFILE(R.drawable.profile_user, "Profile", DestinationScreen.Profile),
}
@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavigationMenu,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color.White)
    ) {
        for (item in BottomNavigationMenu.values()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { navigateTo(navController, item.navDestination.route) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp),
                    colorFilter = if (item == selectedItem) {
                        ColorFilter.tint(color = Color.Black)
                    } else {
                        ColorFilter.tint(color = Color.Gray)
                    }
                )
                Text(
                    text = item.label,
                    color = if (item == selectedItem) Color.Black else Color.Gray

                )
            }
        }
    }
}