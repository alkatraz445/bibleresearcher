package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mandk.biblereasercher.R


val robotoFontFamily = FontFamily(
    Font(R.font.font_roboto_light, FontWeight.Light),
    Font(R.font.font_roboto_regular, FontWeight.Normal),
    Font(R.font.font_roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.font_roboto_medium, FontWeight.Medium),
    Font(R.font.font_roboto_bold, FontWeight.Bold)
)

val baskerFontFamily = FontFamily(
    Font(R.font.font_basker_regular, FontWeight.Normal),
    Font(R.font.font_basker_italic, FontWeight.Normal, FontStyle.Italic)
)

val AppTypography =  Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = baskerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Left,
        lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.None),

    ),
    bodyLarge = TextStyle(
        fontFamily = baskerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 64.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.None)
    ),
    bodyMedium = TextStyle(
        fontFamily = baskerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 15.sp,
        letterSpacing = (-0.75).sp,
        textAlign = TextAlign.Left,
        lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.None)
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center,

    ),
)
