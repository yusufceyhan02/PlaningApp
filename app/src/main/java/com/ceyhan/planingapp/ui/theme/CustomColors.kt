package com.ceyhan.planingapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf

val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }

data class CustomColors(
    val blue: Color,
    val blueHover: Color,
    val green: Color,
    val greenHover: Color,
    val orange: Color,
    val orangeHover: Color,
    val red: Color,
    val redHover: Color,
    val gray: Color,
    val grayHover: Color,
    val circleColor: Color,
    val lineColor: Color,
    val reverseColor: Color
)

val LightCustomColors = CustomColors(
    blue = Color(0xFF0056A3),
    blueHover = Color(0xFFD0E9FF),
    green = Color(0xFF1D6F2F),
    greenHover = Color(0xFFD4EDDA),
    orange = Color(0xFFE68900),
    orangeHover = Color(0xFFFFF4DB),
    red = Color(0xFFA82B34),
    redHover = Color(0xFFF8D7DA),
    gray = Color(0xFF495057),
    grayHover = Color(0xFFE9ECEF),
    circleColor = Color(0xFF495057),
    lineColor = Color(0xFFE9ECEF),
    reverseColor = Color(0xFF000000)
)

val DarkCustomColors = CustomColors(
    blue = Color(0xFF2978D1),
    blueHover = Color(0xFF264D73),
    green = Color(0xFF3B8238),
    greenHover = Color(0xFF1F4432),
    orange = Color(0xFFE08400),
    orangeHover = Color(0xFF664A00),
    red = Color(0xFFC35A5A),
    redHover = Color(0xFF4A1C22),
    gray = Color(0xFF6C757D),
    grayHover = Color(0xFF343A40),
    circleColor = Color(0xFFDEE2E6),
    lineColor = Color(0xFF343A40),
    reverseColor = Color(0xFFFFFFFF)
)
