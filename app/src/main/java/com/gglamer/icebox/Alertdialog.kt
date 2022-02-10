package com.gglamer.icebox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch


@Composable
fun Alert(boolean: MutableState<Boolean>, color: Color, text: String) {
    if (boolean.value){
        AlertDialog(
            onDismissRequest = { boolean.value = false },
            text ={
                Text(text = text, fontSize = 20.sp, textAlign = TextAlign.Center)
            },
            confirmButton = {
                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        boolean.value = false
                              },
                ) {
                    Text("OK", fontSize = 15.sp, textAlign = TextAlign.Center)
                }
            },
            backgroundColor = color
        )
    }
}
@Composable
fun Changename(boolean: MutableState<Boolean>, color: Color, key:String, token:String) {
    var modulename = remember { mutableStateOf<String>("") }
    val client = HttpClient(CIO)
    val composableScope = rememberCoroutineScope()
    var url = "https://smarticebox.herokuapp.com/api/module/update/${key}/"
    if (boolean.value){
        AlertDialog(
            onDismissRequest = { boolean.value = false },
            text ={
                OutlinedTextField(modifier = Modifier
                    .fillMaxWidth(),
                    value = modulename.value, onValueChange = {modulename.value = it})
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    onClick = {
                        boolean.value = false
                    },
                ) {
                    Text("Back", fontSize = 15.sp, textAlign = TextAlign.Center)
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                    onClick = {
                        boolean.value = false
                        composableScope.launch {
                            try {
                                val httpResponse: HttpResponse = client.put(url) {
                                    headers {
                                        append(
                                            HttpHeaders.Authorization,
                                            "Token ${token}"
                                        )
                                        append(
                                            HttpHeaders.ContentType,
                                            "application/x-www-form-urlencoded"
                                        )
                                    }
                                    body = "name=${modulename.value}"
                                }

                            } catch (e: ClientRequestException) {
                                println(e.response)

                            }
                        }
                    },
                ) {
                    Text("Save", fontSize = 15.sp, textAlign = TextAlign.Center)
                }
            },
            backgroundColor = color
        )
    }
}

