package com.example.chattingapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Gray)) {
        Text(text = "holle login page")

    }
}