package com.pal.chattingapplication.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pal.chattingapplication.R
import com.pal.chattingapplication.LCViewModel
import com.pal.chattingapplication.components.ButtonComponentLogin
import com.pal.chattingapplication.components.CheckSignedIn
import com.pal.chattingapplication.components.ClickableLoginTextComponent
import com.pal.chattingapplication.components.CommonProgressBar
import com.pal.chattingapplication.components.DividerTextComponent
import com.pal.chattingapplication.components.HeadingTextComponent
import com.pal.chattingapplication.components.MyTextFieldComponent
import com.pal.chattingapplication.components.NormalTextComponent

@Composable
fun LoginScreen(vm: LCViewModel, navController: NavController){
    val context = LocalContext.current
    CheckSignedIn(vm = vm, navController = navController)

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

                MyTextFieldComponent(labelValue = "Email", painterResource = painterResource(id = R.drawable.message),
                    onTextChanged = {email.value = it}
                )
                MyTextFieldComponent(labelValue = "Password", painterResource = painterResource(id = R.drawable.lock),
                    onTextChanged = {password.value = it}
                )

                Spacer(modifier = Modifier.height(40.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Forgot Password", modifier = Modifier.clickable {
                        if (email.value.isEmpty()){
                            Toast.makeText(context, "Please enter the email", Toast.LENGTH_SHORT).show()
                        }else{
                        vm.forgetPassword(email=email.value)
                        Toast.makeText(context,"Confirmation email sent", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponentLogin(vm = vm, email = email.value, password = password.value)
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()
                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {

                }, navController = navController)
            }

        }

    }
    if (vm.inProcess.value){
        CommonProgressBar()
    }
}

@Preview
@Composable
fun LoginPagePreview(){
    LoginScreen(vm = hiltViewModel<LCViewModel>(), navController = rememberNavController())
}