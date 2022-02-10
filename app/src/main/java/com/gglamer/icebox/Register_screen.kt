package com.gglamer.icebox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gglamer.destinations.login_screenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception


@Destination
@Composable
fun register_screen(
    navigator: DestinationsNavigator

) {
    val client = HttpClient(CIO)
    val composableScope = rememberCoroutineScope()
    val url = "https://smarticebox.herokuapp.com/api/user/"
    var register_is_suc = remember { mutableStateOf<Boolean>(false)}
    var register_error = remember { mutableStateOf<Boolean>(false)}
    var error_code = remember { mutableStateOf("") }
    var first_name_value = remember { mutableStateOf("") }
    var last_name_value = remember {mutableStateOf("")}
    var username_value = remember { mutableStateOf("") }
    var email_value = remember { mutableStateOf("") }
    var password_value = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Column(

            modifier = Modifier
                .width(280.dp)
                .height(550.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
            Text(
                modifier = Modifier
                    .padding(top = 90.dp)
                    .align(
                        alignment =
                        Alignment.Start
                    ),
                text = "Регистрация",
                fontSize = 30.sp,
                textAlign = TextAlign.Left
            )
            OutlinedTextField(
                value = first_name_value.value,
                onValueChange = { first_name_value.value = it },
                label = {
                    Text(
                        text = "Введите имя"
                    )
                })
            OutlinedTextField(
                value = last_name_value.value,
                onValueChange = { last_name_value.value = it },
                label = {
                    Text(
                        text = "Введите фамилию"
                    )
                },
            )
            OutlinedTextField(
                value = username_value.value,
                onValueChange = { username_value.value = it },
                label = {
                    Text(
                        text = "Введите имя пользователя"
                    )
                },
            )
            OutlinedTextField(
                value = email_value.value,
                onValueChange = { email_value.value = it },
                label = {
                    Text(
                        text = "Введите email"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = password_value.value,
                onValueChange = { password_value.value = it },
                label = {
                    Text(
                        text = "Введите пароль"
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Alert(boolean = register_is_suc, color = Color(0xFF96CEB4), text = "Регистрация прошла успешна. Пожалуйста проверте свою почту")
            Alert(boolean = register_error, color = Color(0xFFD9534F), text = error_code.value)
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),onClick = {
                if(email_value.value.indexOf("@") == -1){
                    register_error.value = true
                    error_code.value = "Пожалуйста введите корректный емаил"
                }
                else if(password_value.value == "" || username_value.value == "" || first_name_value.value == "" || last_name_value.value == ""){
                    register_error.value = true
                    error_code.value = "Поля не должно быть пустым"
                }
                else{
                    composableScope.launch {
                        try {
                            val httpResponse: HttpResponse = client.post(url) {
                                headers {
                                    append(
                                        HttpHeaders.ContentType,
                                        "application/x-www-form-urlencoded"
                                    )
                                }
                                body = "username=${username_value.value}&first_name=${first_name_value.value}&last_name=${last_name_value.value}&email=${email_value.value}&password=${password_value.value}"
                            }
                            val stringBody: String = httpResponse.receive()
                            login_value.value = username_value.value
                            navigator.navigate(login_screenDestination)
                            println(stringBody)
                            register_is_suc.value = true

                        } catch (e: ClientRequestException) {
                            val response: String = e.response.receive()
                            val responseJson = JSONObject(response)
                            try {
                                val username = responseJson.getJSONArray("username").getString(0)
                                error_code.value = username
                                register_error.value = true
                            }catch (e: Exception){
                                error_code.value = e.toString()
                                register_error.value = true
                            }
                        }
                }
                }
            }, ) {
                Text(text = "Зарегистрироваться", textAlign = TextAlign.Center)
            }
            ClickableText(modifier = Modifier.align(alignment = Alignment.Start),text = buildAnnotatedString { withStyle(style = SpanStyle(color = Color.Blue)){
                append("Авторизация")
            } },onClick = {
                navigator.navigate(login_screenDestination)
            })
        }
    }
}
