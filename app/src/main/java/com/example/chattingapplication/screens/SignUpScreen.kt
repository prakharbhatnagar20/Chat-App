package com.example.chattingapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chattingapplication.LCViewModel
import com.example.chattingapplication.R
import com.example.chattingapplication.components.ButtonComponent
import com.example.chattingapplication.components.CheckSignedIn

import com.example.chattingapplication.components.ClickableLoginTextComponent
import com.example.chattingapplication.components.CommonProgressBar
import com.example.chattingapplication.components.DividerTextComponent
import com.example.chattingapplication.components.HeadingTextComponent
import com.example.chattingapplication.components.MyTextFieldComponent
import com.example.chattingapplication.components.NormalTextComponent


@Composable
fun SignUpScreen(navController: NavController, vm: LCViewModel){
    CheckSignedIn(vm = vm, navController = navController)
    val firstName = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Surface(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {
                NormalTextComponent(value = "Hey there,")
                HeadingTextComponent(value = "Create an Account")
                Spacer(modifier = Modifier.height(20.dp))
                MyTextFieldComponent(labelValue = "Name", painterResource = painterResource(id = R.drawable.profile),
                    onTextChanged = {firstName.value = it}
                )
                MyTextFieldComponent(labelValue = "Number", painterResource = painterResource(id = R.drawable.profile),
                    onTextChanged = {number.value = it}
                )
                MyTextFieldComponent(labelValue = "Email", painterResource = painterResource(id = R.drawable.message),
                    onTextChanged = {email.value = it}
                )
                MyTextFieldComponent(labelValue = "Password", painterResource = painterResource(id = R.drawable.lock),
                    onTextChanged = {password.value = it}
                )

                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponent(
                   navController, vm, name = firstName.value ,email = email.value, password = password.value, number = number.value

                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()
                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {

                }, navController = navController)
            }

        }

    }
//    if (vm.inProcess.value){
//        CommonProgressBar()
//    }


}
//@Preview
//@Composable
//fun DefaultPreviewOfSignUpScreen() {
//    SignUpScreen(navController = rememberNavController())
//}
