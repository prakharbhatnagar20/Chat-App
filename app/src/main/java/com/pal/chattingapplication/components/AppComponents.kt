package com.pal.chattingapplication.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.pal.chattingapplication.R
import com.pal.chattingapplication.DestinationScreen
import com.pal.chattingapplication.navigateTo
import com.pal.chattingapplication.ui.theme.GrayColor
import com.pal.chattingapplication.ui.theme.Primary
import com.pal.chattingapplication.ui.theme.Secondary
import com.pal.chattingapplication.ui.theme.TextColor
import com.pal.chattingapplication.ui.theme.componentShapes
import com.pal.chattingapplication.LCViewModel

@Composable
fun NormalTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        textAlign = TextAlign.Center
    )
}
@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ), color = colorResource(id = R.color.colorText),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MyTextFieldComponent(
    labelValue: String, painterResource: Painter, onTextChanged: (String) -> Unit
) {

    val textValue = remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small),
        label = { Text(text = labelValue) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
    )
}

@Composable
fun ButtonComponent(navController: NavController,vm: LCViewModel,name: String ,email: String, password: String, number: String){
    val context = LocalContext.current
   Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
                    if (vm.validate(name,password,email,number)){
                        vm.signUp(name, email, password, number)
                    }else{
                        Toast.makeText(context, "Invalid email or mobile number", Toast.LENGTH_SHORT).show()
                    }
//            navigateTo(navController = navController, DestinationScreen.ChatList.route)

        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign Up",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

    }
}
@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = "or",
            fontSize = 18.sp,
            color = TextColor
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )
    }
}

@Composable
fun ClickableLoginTextComponent(tryingToLogin: Boolean = true, onTextSelected: (String) -> Unit, navController: NavController) {
    val initialText =
        if (tryingToLogin) "Already have an account? " else "Don’t have an account yet? "
    val loginText = if (tryingToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { if (tryingToLogin){
            navigateTo(navController, DestinationScreen.Login.route)
        }else
        {
            navigateTo(navController, DestinationScreen.SignUp.route)
        }

        },
    )
}
@Composable
fun ClickableLoginTextComponentLogin(tryingToLogin: Boolean, onTextSelected: (String) -> Unit, navController: NavController) {
    val initialText =
        if (tryingToLogin) "Already have an account? " else "Don’t have an account yet? "
    val loginText = if (tryingToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { navigateTo(navController, DestinationScreen.Login.route)

        },
    )
}


@Composable
fun CommonProgressBar(){
    Row(modifier = Modifier
        .alpha(0.5f)
        .background(Color.Gray)
        .clickable(false) { }
        .fillMaxSize(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}
@Composable
fun CheckSignedIn(vm: LCViewModel, navController: NavController){
    val alreadySignIn = remember {
        mutableStateOf(false)
    }
    val signIn = vm.signIn.value
    if(signIn&&!alreadySignIn.value){
        alreadySignIn.value = true
        navController.navigate(DestinationScreen.ChatList.route){
            popUpTo(0)
        }
    }
}
@Composable
fun ButtonComponentLogin(vm: LCViewModel,email: String, password: String){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            Log.d("TAG", "login clicked")
            vm.logIn(email, password)
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Log In",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@Composable
fun CommonImage(data: String?,
                modifier: Modifier=Modifier.wrapContentSize(),
                contentScale: ContentScale = ContentScale.Crop){
    val painter = rememberAsyncImagePainter(model = data)
    Image(painter= painter,contentDescription = null,modifier=modifier,contentScale = contentScale)


}

@Composable
fun CommonDivider(){
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(0.3f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}
@Composable
fun TextTitle(txt: String){
    Column {
        Text(
            text = txt,
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            modifier = Modifier.padding(8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Red Box for far friends
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Red)
            )
            Spacer(modifier = Modifier.width(4.dp)) // Small space between the box and text
            Text(text = "Far Friends", fontSize = 16.sp)

            Spacer(modifier = Modifier.width(16.dp)) // Space between the two indicators

            // Blue Box for closer friends
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Blue)
            )
            Spacer(modifier = Modifier.width(4.dp)) // Small space between the box and text
            Text(text = "Closer Friends", fontSize = 16.sp)
        }
    }

}
@Composable
fun CommonRow(imageUrl: String?, name: String?,textColor: Color ,onItemCLick:()-> Unit){

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(75.dp)
        .background(textColor)
        .clickable { onItemCLick.invoke() },
        verticalAlignment = Alignment.CenterVertically) {
        CommonImage(data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray))
        Text(text = name ?: "---",
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))

    }
    

}