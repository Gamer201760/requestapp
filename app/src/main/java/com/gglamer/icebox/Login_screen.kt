package com.gglamer.icebox


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gglamer.destinations.MainScreenDestination
import com.gglamer.destinations.register_screenDestination
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


var login_value = mutableStateOf("")
var password_value = mutableStateOf("")
@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun login_screen(
    navigator: DestinationsNavigator
) {
    val client = HttpClient(CIO)
    val composableScope = rememberCoroutineScope()
    val url = "https://smarticebox.herokuapp.com/api/token/"
    val context = LocalContext.current
    val dataStore = StoreTokenData(context)
    var password_error = remember { mutableStateOf<Boolean>(false)}
    var login_success = remember { mutableStateOf<Boolean>(false)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {


        Text(
            modifier = Modifier.padding(top = 130.dp),
            text = "Авторизация",
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .width(280.dp)
                .height(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly

        ) {

            OutlinedTextField(
                value = login_value.value,
                onValueChange = { login_value.value = it },
                label = {
                    Text(
                        text = "Введите логин"
                    )
                })
            OutlinedTextField(
                value = password_value.value,
                onValueChange = { password_value.value = it },
                label = {
                    Text(
                        text = "Введите пароль"
                    )
                },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    composableScope.launch {
                        try {
                            val httpResponse: HttpResponse = client.post(url) {
                                headers {
                                    append(
                                        HttpHeaders.ContentType,
                                        "application/x-www-form-urlencoded"
                                    )
                                }
                                body = "username=${login_value.value}&password=${password_value.value}"
                            }
                            val stringBody: String = httpResponse.receive()
                            val token = JSONObject(stringBody).get("token").toString()
                            dataStore.saveToken(token)
                            navigator.navigate(MainScreenDestination(token))
                            login_success.value = true

                        } catch (e: ClientRequestException) {
                            println(e.response)
                            password_error.value = true
                            dataStore.saveToken("error")
                        }
                    }
                },
            ) {
                Text(text = "Войти", textAlign = TextAlign.Center)
            }
            Alert(boolean = password_error, color = Color(0xFFD9534F), text = "Пароль или юзернаме не верный")
            Alert(boolean = login_success, color = Color(0xFF96CEB4), text = "Логин прошёл успешно")
            val token = dataStore.getToken.collectAsState(initial = "")
            ClickableText(modifier = Modifier.align(alignment = Start),text = buildAnnotatedString { withStyle(style = SpanStyle(color = Color.Blue)){
                append("Регистрация")
            } },onClick = {
                navigator.navigate(register_screenDestination)
            })
            ClickableText(modifier = Modifier.align(alignment = Start),text = buildAnnotatedString { withStyle(style = SpanStyle(color = Color.Blue)){
                append("Забыли пароль")
            } },onClick = {

                println(token.value!!)
            })

        }
    }
}
