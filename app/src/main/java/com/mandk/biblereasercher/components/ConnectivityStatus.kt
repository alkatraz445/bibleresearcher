package com.mandk.biblereasercher.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ConnectivityStatus(isConnected: Boolean) {
    var visibility by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ConnectivityStatusBox(isConnected = isConnected)
    }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            visibility = true
        } else {
            delay(2000)
            visibility = false
        }
    }
}

@Composable
fun ConnectivityStatusBox(isConnected: Boolean) {
    val colorGreen = Color(0xFF00C853)
    val colorRed = Color(0xFFD43737)
    val backgroundColor by animateColorAsState(if (isConnected) colorGreen else colorRed,
        label = ""
    )
    val message = if (isConnected) "Back Online!" else "No Internet Connection!"
//    val iconResource = if (isConnected) R.drawable.ic_connectivity_available else R.drawable.ic_connectivity_unavailable

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
//        TODO: add an icon
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(painterResource(id = iconResource), "Connectivity Icon", tint = Color.White)
//            Spacer(modifier = Modifier.size(8.dp))
//            Text(message, color = Color.White, fontSize = 15.sp)
//        }
    }
}