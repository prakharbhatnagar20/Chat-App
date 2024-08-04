package com.pal.chattingapplication.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pal.chattingapplication.DestinationScreen
import com.pal.chattingapplication.LCViewModel
import com.pal.chattingapplication.components.CommonDivider
import com.pal.chattingapplication.components.CommonImage
import com.pal.chattingapplication.navigateTo
import com.pal.chattingapplication.ui.theme.ChattingApplicationTheme

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel){
    val userData = vm.userData.value
    var name by rememberSaveable {
        mutableStateOf(userData?.name?:"")
    }
    var number by rememberSaveable {
        mutableStateOf(userData?.number?:"")
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start =8.dp, bottom = 56.dp, end = 8.dp) // Add padding equal to the BottomNavigation's height
                .verticalScroll(rememberScrollState())
        ) {
            ProfileContent(
                modifier = Modifier.padding(8.dp),
                vm = vm,
                name = name,
                number = number,
                onNameChange = { name = it },
                onNumberChange = { number = it },
                onSave = {
                    vm.createOrUpdateProfile(
                        name = name, number = number
                    )
                },
                onBack = {
                    navigateTo(
                        navController = navController,
                        route = DestinationScreen.ChatList.route
                    )
                },
                onLogout = {
                    vm.logout()
                    navigateTo(navController = navController, route = DestinationScreen.Login.route)
                }
            )
        }

        BottomNavigationMenu(
            selectedItem = BottomNavigationMenu.PROFILE,
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

@Composable
fun ProfileContent(modifier: Modifier,vm:LCViewModel,name:String,
                   number:String,onNameChange:(String)->Unit,
                   onNumberChange:(String)->Unit,
                   onBack: ()->Unit,
                   onSave: ()-> Unit,
                   onLogout:()-> Unit){
    val imageUrl = vm.userData.value?.imageUrl
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Back", Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save", Modifier.clickable {
                onSave.invoke()
            })}
            CommonDivider()
            ProfileImage(vm=vm, imageUrl = imageUrl)
            CommonDivider()
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Name", modifier = Modifier.width(100.dp))
                TextField(value = name, onValueChange = onNameChange, colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ))

            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Number", modifier = Modifier.width(100.dp))
                TextField(value = number, onValueChange = onNumberChange, colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ))

            }

            CommonDivider()
        Spacer(modifier = Modifier.height(50.dp))

        Spacer(modifier = Modifier.height(50.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(40.dp)
                .background(Color.Black)
                .clickable {
                    onLogout.invoke()
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "LogOut", color = Color.White)

            }

    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }

        
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            },
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)) {
                CommonImage(data = imageUrl)

            }
            Text(text = "Change Profile Picture")

        }
    }
}
@Preview
@Composable
fun ProfileScreenPreview(){
    ChattingApplicationTheme {
        ProfileScreen(navController = rememberNavController(), vm =  hiltViewModel<LCViewModel>())
    }
}
