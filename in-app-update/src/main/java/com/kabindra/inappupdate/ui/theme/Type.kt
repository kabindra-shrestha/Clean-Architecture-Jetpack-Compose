package com.kabindra.inappupdate.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun AppTypography() = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontSize = 57.ssp),
        displayMedium = displayMedium.copy(fontSize = 45.ssp),
        displaySmall = displaySmall.copy(fontSize = 36.ssp),

        headlineLarge = headlineLarge.copy(fontSize = 32.ssp),
        headlineMedium = headlineMedium.copy(fontSize = 28.ssp),
        headlineSmall = headlineSmall.copy(fontSize = 24.ssp),

        titleLarge = titleLarge.copy(fontSize = 22.ssp),
        titleMedium = titleMedium.copy(fontSize = 16.ssp),
        titleSmall = titleSmall.copy(fontSize = 14.ssp),

        bodyLarge = bodyLarge.copy(fontSize = 16.ssp),
        bodyMedium = bodyMedium.copy(fontSize = 14.ssp),
        bodySmall = bodySmall.copy(fontSize = 12.ssp),

        labelLarge = labelLarge.copy(fontSize = 14.ssp),
        labelMedium = labelMedium.copy(fontSize = 12.ssp),
        labelSmall = labelSmall.copy(fontSize = 11.ssp),
    )
}