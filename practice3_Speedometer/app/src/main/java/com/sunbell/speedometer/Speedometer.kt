package com.sunbell.speedometer

import android.view.MotionEvent
import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private val INDICATOR_LENGTH = 14.dp
private val MAJOR_INDICATOR_LENGTH = 18.dp
private val INDICATOR_INITIAL_OFFSET = 5.dp

enum class DrivingMode {
    PARK, REVERSE, NEUTRAL, DRIVE
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpeedometerScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val speed = remember { Animatable(0f) }
        val drivingMode = remember { mutableStateOf(DrivingMode.PARK) }
        val isOperating = remember { mutableStateOf(false) } // 엑셀 or 브레이크 중인지?
        val drivingModeValue = 30f // 최소 유지
        val maxSpeed = 240f
        val minSpeed = 0f
        val speedIncrement = 1.5f // 가속, 감속량
        val speedDecrement = 0.2f // 자동 감속량

        Speedometer(
            currentSpeed = speed.value,
            modifier = Modifier
                .padding(top = 90.dp, end = 90.dp, start = 90.dp, bottom = 30.dp)
                .requiredSize(360.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // P(주차) 모드 버튼
            Button(
                onClick = { if (speed.value == 0f) drivingMode.value = DrivingMode.PARK },
                enabled = speed.value == 0f,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (drivingMode.value == DrivingMode.PARK) Color.Black else Color.Gray
                )
            ) { Text("P") }

            Spacer(modifier = Modifier.width(8.dp)) // 버튼 사이 간격

            // R(후진) 모드 버튼
            Button(
                onClick = { drivingMode.value = DrivingMode.REVERSE },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (drivingMode.value == DrivingMode.REVERSE) Color.Black else Color.Gray
                )
            ) { Text("R") }

            Spacer(modifier = Modifier.width(8.dp))

            // N(중립) 모드 버튼
            Button(
                onClick = { drivingMode.value = DrivingMode.NEUTRAL },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (drivingMode.value == DrivingMode.NEUTRAL) Color.Black else Color.Gray
                )
            ) { Text("N") }

            Spacer(modifier = Modifier.width(8.dp))

            // D(드라이빙) 모드 버튼
            Button(
                onClick = { drivingMode.value = DrivingMode.DRIVE },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (drivingMode.value == DrivingMode.DRIVE) Color.Black else Color.Gray
                )
            ) { Text("D") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // 엑셀 버튼
            Button(
                onClick = {},
                enabled = drivingMode.value != DrivingMode.PARK,
                modifier = Modifier
                    .weight(1f)
                    .pointerInteropFilter {
                        if (drivingMode.value == DrivingMode.PARK) {
                            false // P 모드일 때는 엑셀 작동 중지
                        } else {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    isOperating.value = true
                                    coroutineScope.launch {
                                        while (currentCoroutineContext().isActive) {
                                            // 속도에 따른 speedIncrement 조정
                                            val currentIncrement = when {
                                                speed.value >= 220f -> 0.05f
                                                speed.value >= 200f -> 0.1f
                                                speed.value >= 180f -> 0.3f
                                                speed.value >= 160f -> 0.5f
                                                speed.value >= 140f -> 0.7f
                                                speed.value >= 120f -> 0.8f
                                                speed.value >= 100f -> 1f
                                                else -> speedIncrement
                                            }
                                            speed.animateTo(
                                                (speed.value + currentIncrement).coerceAtMost(maxSpeed),
                                                animationSpec = tween(durationMillis = 10)
                                            )
                                        }
                                    }
                                }
                                MotionEvent.ACTION_UP -> {
                                    isOperating.value = false
                                    coroutineScope.coroutineContext.cancelChildren()
                                }
                            }
                            true
                        }
                    }
            ) {
                Text("엑셀")
            }

            Spacer(modifier = Modifier.width(32.dp))

            // 브레이크 버튼
            Button(
                onClick = {},
                enabled = drivingMode.value != DrivingMode.PARK,
                modifier = Modifier
                    .weight(1f)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                isOperating.value = true
                                coroutineScope.launch {
                                    while (currentCoroutineContext().isActive) {
                                        speed.animateTo(
                                            (speed.value - speedIncrement).coerceAtLeast(minSpeed),
                                            animationSpec = tween(durationMillis = 10)
                                        )
                                    }
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                isOperating.value = false
                                coroutineScope.coroutineContext.cancelChildren()
                            }
                        }
                        true
                    }
            ) {
                Text("브레이크")
            }
        }

        // 자동 감속 및 모드에 따른 로직
        LaunchedEffect(speed.value, isOperating.value,drivingMode.value) {
            when (drivingMode.value) {
                DrivingMode.PARK -> {

                /* 주차 모드일 때는 불필요 */

                }

                DrivingMode.REVERSE -> {
                    if (!isOperating.value && speed.value > drivingModeValue) {
                        delay(10)
                        coroutineScope.launch {
                            speed.animateTo(
                                (speed.value - speedDecrement).coerceAtLeast(drivingModeValue),
                                animationSpec = tween(durationMillis = 50)
                            )
                        }
                    }
                }

                DrivingMode.NEUTRAL -> {
                    if (!isOperating.value && speed.value > minSpeed) {
                        delay(10)
                        coroutineScope.launch {
                            speed.animateTo(
                                (speed.value - (speedDecrement * 2)).coerceAtLeast(minSpeed), // 더빨리 감속
                                animationSpec = tween(durationMillis = 50)
                            )
                        }
                    }
                }

                DrivingMode.DRIVE -> {
                    if (!isOperating.value && speed.value > drivingModeValue) {
                        delay(10)
                        coroutineScope.launch {
                            speed.animateTo(
                                (speed.value - speedDecrement).coerceAtLeast(drivingModeValue),
                                animationSpec = tween(durationMillis = 50)
                            )
                        }
                    }
                }

            }
        }

        Slider(
            value = speed.value,
            valueRange = 0f..240f,
            onValueChange = {
                coroutineScope.launch {
                    speed.animateTo(
                        it,
                        animationSpec = tween(durationMillis = 50, easing = LinearOutSlowInEasing)
                    )
                }
            },
            modifier = Modifier.padding(16.dp),
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun Speedometer(
    @FloatRange(from = 0.0, to = 240.0) currentSpeed: Float,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val textColor = MaterialTheme.colorScheme.onSurface
    val indicatorColor = MaterialTheme.colorScheme.onSurface
    Canvas(modifier = modifier, onDraw = {
        drawArc(
            color = Color.Red,
            startAngle = 30f,
            sweepAngle = -240f,
            useCenter = false,
            style = Stroke(width = 2.0.dp.toPx())
        )

        for (angle in 300 downTo 60 step 2) {
            val speed = 300 - angle

            val startOffset =
                pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_INITIAL_OFFSET.toPx(),
                    cX = center.x,
                    cY = center.y
                )

            if (speed % 20 == 0) {
                val markerOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                speedMarker(startOffset, markerOffset, SolidColor(indicatorColor), 4.dp.toPx())
                speedText(
                    speed = speed,
                    angle = angle,
                    textMeasurer = textMeasurer,
                    textColor = textColor
                )
            } else if (speed % 10 == 0) {
                val endOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                speedMarker(startOffset, endOffset, SolidColor(Color.Blue), 2.dp.toPx())
            } else {
                val endOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                speedMarker(startOffset, endOffset, SolidColor(Color.Blue), 1.dp.toPx())
            }
        }

        speedIndicator(speedAngle = 300 - currentSpeed)
    })
}

private fun DrawScope.speedMarker(
    startPoint: Offset,
    endPoint: Offset,
    brush: Brush,
    strokeWidth: Float
) {
    drawLine(brush = brush, start = startPoint, end = endPoint, strokeWidth = strokeWidth)
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.speedText(
    speed: Int,
    angle: Int,
    textColor: Color,
    textMeasurer: TextMeasurer
) {
    val textLayoutResult = textMeasurer.measure(
        text = speed.toString(),
        style = TextStyle.Default.copy(lineHeight = TextUnit(0.0f, TextUnitType.Sp))
    )
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height

    val textOffset = pointOnCircle(
        thetaInDegrees = angle.toDouble(),
        radius = size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx() - textWidth / 2 - INDICATOR_INITIAL_OFFSET.toPx(), // Adjusting radius with text width
        cX = center.x,
        cY = center.y
    )

    drawContext.canvas.save()
    // Translate to the text offset point, adjusting for vertical centering.
    drawContext.canvas.translate(
        textOffset.x - textWidth / 2,
        textOffset.y - textHeight / 2
    )

    drawText(textLayoutResult, color = textColor)

    drawContext.canvas.restore()
}

private fun DrawScope.speedIndicator(
    speedAngle: Float
) {
    val endOffset = pointOnCircle(
        thetaInDegrees = speedAngle.toDouble(),
        radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
        cX = center.x,
        cY = center.y
    )

    drawLine(
        color = Color.Magenta,
        start = center,
        end = endOffset,
        strokeWidth = 6.dp.toPx(),
        cap = StrokeCap.Round,
        alpha = 0.5f
    )
}

private fun pointOnCircle(
    thetaInDegrees: Double,
    radius: Float,
    cX: Float = 0f,
    cY: Float = 0f
): Offset {
    val x = cX + (radius * sin(Math.toRadians(thetaInDegrees)).toFloat())
    val y = cY + (radius * cos(Math.toRadians(thetaInDegrees)).toFloat())

    return Offset(x, y)
}