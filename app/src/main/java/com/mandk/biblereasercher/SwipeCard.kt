package com.mandk.biblereasercher

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun SwipeCard(
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    swipeThreshold: Float = 450f,
    sensitivityFactor: Float = 3f,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableFloatStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density

    val backgroundImage = painterResource(id = R.drawable.read)


    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            delay(300)
            onSwipeRight.invoke()
            dismissRight = false
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            delay(300)
            onSwipeLeft.invoke()
            dismissLeft = false
        }
    }



    Box(modifier = Modifier) {
        Image(
            painter = backgroundImage, contentDescription = null, modifier = Modifier
                .fillMaxSize()
                .scale(1.5f)
        )
        Box(modifier = Modifier
            .offset { IntOffset(offset.roundToInt(), 0) }
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onDragEnd = {
                    offset = 0f
                }) { change, dragAmount ->

                    offset += (dragAmount / density) * sensitivityFactor
                    when {
                        offset > swipeThreshold -> {
                            dismissRight = true

                        }

                        offset < -swipeThreshold -> {
                            dismissLeft = true

                        }
                    }
                    if (change.positionChange() != Offset.Zero) change.consume()
                }
            }
            .graphicsLayer(
                alpha = 1f - animateFloatAsState(
                    if (dismissRight) 1f else 0f,
                    label = ""
                ).value,
            )
        )
        {
            content()
        }
    }
}