package com.gglamer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.gglamer.destinations.MainScreenDestination
import com.gglamer.destinations.login_screenDestination
import com.gglamer.icebox.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

var counter = 0
@Destination(start = true)
@ExperimentalFoundationApi
@Composable
fun Navigation(
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current
    val dataStore = StoreTokenData(context)
    val tok = dataStore.getToken.collectAsState(initial = "")
    val token = tok.value!!

    if (token.length == 40) {
        navigator.navigate(MainScreenDestination(token))
    }else{
        counter++
        if (counter >= 3) {
            println("login")
            navigator.navigate(login_screenDestination)
            counter = 0
            return
        }
    }
}
