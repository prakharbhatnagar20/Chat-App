package com.pal.chattingapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pal.chattingapplication.DestinationScreen
import com.pal.chattingapplication.LCViewModel
import com.pal.chattingapplication.components.CommonRow
import com.pal.chattingapplication.components.TextTitle
import com.pal.chattingapplication.navigateTo

@Composable
fun ChatListScreen(navController: NavController, vm: LCViewModel){

       val chats = vm.chats.value
    val userData = vm.userData.value
    val showDialog = remember {
        mutableStateOf(false)
    }
    val onFabClick:()->Unit={showDialog.value=true}
    val onDismiss:()->Unit={showDialog.value=false}
    val onAddChat:(String)->Unit={
        vm.onAddChat(it)
        showDialog.value = false
    }
    Scaffold(
        floatingActionButton ={ FAB(
            showDialog = showDialog.value,
            onFabClick = onFabClick,
            onDismiss = onDismiss,
            onAddChat = onAddChat
        )},
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                TextTitle(txt = "Chats")
                if (chats.isEmpty()){
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Text(text = "No Chats Available")

                    }
                }else{
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chats){
                            chat->
                            val  chatUser = if(chat.user1.userId== userData?.userId){
                                chat.user2
                            }else{
                                chat.user1
                            }
                            val locationMatch =
                                chat.user1.location?.let { it1 -> chat.user2.location?.let { it2 ->
                                    vm.isLocationMatch(it1,
                                        it2
                                    )
                                } }
                            val textColor = if (locationMatch == true) {
                                Color(0xFFCCD5FF) // Color when locations match
                            } else {
                                Color(0xFFC884A6) // Color when locations do not match
                            }
                            CommonRow(imageUrl = chatUser.imageUrl, name =chatUser.name, textColor=  textColor) {
                                chat.chatId?.let{
                                    navigateTo(navController, DestinationScreen.SingleChat.createRoute(id = it))

                                }

                            }
                        }

                    }
                }
                BottomNavigationMenu(
                    selectedItem = BottomNavigationMenu.CHATLIST,
                    navController = navController,
                    modifier = Modifier
                )

            }

        }
    )

}


@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick:()-> Unit,
    onDismiss:()-> Unit,
    onAddChat:(String)->Unit

){if (showDialog){
    val addChatNumber = remember {
        mutableStateOf("")
    }
        AlertDialog(onDismissRequest = { onDismiss.invoke()
        addChatNumber.value = ""},
            confirmButton = {
                Button(onClick = { onAddChat(addChatNumber.value) }) {
                    Text(text = "Add Chat")
                    
                }
            },
            title = { Text(text = "Add Chat")},
            text = {
                OutlinedTextField(value = addChatNumber.value, label = { Text(text = "Enter Your Friend's Phone No.")} ,onValueChange = {addChatNumber.value = it},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            )}
        FloatingActionButton(
            onClick = { onFabClick.invoke() },
            containerColor = MaterialTheme.colorScheme.secondary,
            shape = CircleShape,
            modifier = Modifier.padding(40.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)

        }



}

