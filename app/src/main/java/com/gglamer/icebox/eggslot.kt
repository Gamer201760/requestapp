package com.gglamer.icebox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun eggslot(egg: Int, name: String, size: Float, onClick: () -> Unit) {

    Box(modifier = Modifier.size(width = size.dp * 130, height = size.dp * 285))
    {
        Spacer(modifier = Modifier.padding(top = size.dp * 27))
        Text(modifier = Modifier
            .fillMaxWidth().padding(bottom = 10.dp).clickable(onClick = onClick)
            ,
            text = name, fontSize = 20.sp, textAlign = TextAlign.Center)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(size.dp * 250)
                .padding(top = 30.dp, start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(color = Color(0xFFA8D8EA)),
        ) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
            ) {
                items(egg){ item ->
                    Card(
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .padding(size.dp * 5)
                            .width(5.dp)
                            .height(size.dp * 45)
                        ,
                        border = BorderStroke(size.dp * 2, Color.Black),
                        backgroundColor = Color.White,
                    ){
                    }
                }
            }
        }

        Text(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            ,text = "$egg/8", fontSize = 20.sp, textAlign = TextAlign.Center)
    }
}
    

