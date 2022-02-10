package com.gglamer.icebox

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gglamer.destinations.login_screenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
var username = mutableStateOf("gglamer")

@Destination
@ExperimentalFoundationApi
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    token: String
) {
    geteggdata(token)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Home is ${username.value}", fontSize = 17.sp)
        Spacer(modifier = Modifier.padding(top=10.dp, bottom = 10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color.Green)
        ){

        }
        Spacer(modifier = Modifier.padding(top=10.dp, bottom = 10.dp))
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .height(height = 285.dp)
            .background(color = Color.Green)
        ){
            println(names.size)
            items(names.size){ item->
                val vis = remember {mutableStateOf<Boolean>(false)}
                eggslot(egg = dates[item].getInt("egg"), names[item], 1F, onClick = { vis.value = true })
                Changename(
                    boolean = vis,
                    color = Color(0xFFFFEDDB),
                    key = keys[item],
                    token = token
                )
            }
        }
        Spacer(modifier = Modifier.padding(top=10.dp, bottom = 10.dp))
        Button(onClick = {
            navigator.navigate(login_screenDestination)
        }) {
            Text(text = "Click")
        }
    }

}


