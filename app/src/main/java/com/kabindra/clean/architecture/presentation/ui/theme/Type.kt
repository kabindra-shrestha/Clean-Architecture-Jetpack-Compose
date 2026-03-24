package com.kabindra.clean.architecture.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.kabindra.clean.architecture.R
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun AppTypography() = Typography().run {
    val fontFamily = ManRopeFontFamily()

    copy(
        displayLarge = displayLarge.copy(fontSize = 57.ssp, fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontSize = 45.ssp, fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontSize = 36.ssp, fontFamily = fontFamily),

        headlineLarge = headlineLarge.copy(fontSize = 32.ssp, fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontSize = 28.ssp, fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontSize = 24.ssp, fontFamily = fontFamily),

        titleLarge = titleLarge.copy(fontSize = 22.ssp, fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontSize = 16.ssp, fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontSize = 14.ssp, fontFamily = fontFamily),

        bodyLarge = bodyLarge.copy(fontSize = 16.ssp, fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontSize = 14.ssp, fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontSize = 12.ssp, fontFamily = fontFamily),

        labelLarge = labelLarge.copy(fontSize = 14.ssp, fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontSize = 12.ssp, fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontSize = 11.ssp, fontFamily = fontFamily),
    )
}

@Composable
fun ManRopeFontFamily() = FontFamily(
    Font(R.font.manrope_light, weight = FontWeight.Light),
    Font(R.font.manrope_regular, weight = FontWeight.Normal),
    Font(R.font.manrope_medium, weight = FontWeight.Medium),
    Font(R.font.manrope_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.manrope_bold, weight = FontWeight.Bold),
    Font(R.font.manrope_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.manrope_extra_light, weight = FontWeight.ExtraLight)
)