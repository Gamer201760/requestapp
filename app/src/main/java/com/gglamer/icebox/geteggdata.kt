package com.gglamer.icebox


import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.sockets.ConnectTimeoutException
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

var keys = mutableStateListOf<String>()
var names = mutableStateListOf<String>()
var dates = mutableStateListOf<JSONObject>()

@Composable
fun geteggdata(token: String) {
    val coroutineScope = rememberCoroutineScope()
    val client = HttpClient(CIO)
    val url = "https://smarticebox.herokuapp.com/api/module/list/"
    var textfilederror = remember { mutableStateOf<Boolean>(false) }
    var textfilederror_message = remember { mutableStateOf("") }
    Alert(textfilederror, color = Color(0xFFD9534F), textfilederror_message.value)
    coroutineScope.launch {
        while (true) {
            println("Token $token")
            try {
                try {
                    val httpResponse: HttpResponse = client.get(url) {
                        headers {
                            append(
                                HttpHeaders.Authorization,
                                "Token $token"
                            )
                        }

                    }
                    val stringBody: String = httpResponse.receive()
                    val json_data = JSONArray(stringBody)

                    if (names.isEmpty()) {
                        for (i in 0 until json_data.length()) {
                            val modulename = json_data.getJSONObject(i).get("name").toString()
                            val key = json_data.getJSONObject(i).get("key").toString()
                            val data = JSONObject(json_data.getJSONObject(i).get("data").toString())

                            keys.add(key)
                            names.add(modulename)
                            dates.add(data)
                        }
                    } else {
                        for (i in 0 until json_data.length()) {
                            val modulename = json_data.getJSONObject(i).get("name").toString()
                            val data = JSONObject(json_data.getJSONObject(i).get("data").toString())
                            val key = json_data.getJSONObject(i).get("key").toString()

                            keys.set(i, key)
                            names.set(i, modulename)
                            dates.set(i, data)
                        }
                    }

                } catch (e: ClientRequestException) {
                    println(e)
//                    println(token)
                }
            }catch (e: ClientRequestException){
                textfilederror_message.value = e.message
                textfilederror.value = true
            }
        }
    }

}


