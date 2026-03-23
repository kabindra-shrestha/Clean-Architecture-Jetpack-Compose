package com.kabindra.clean.architecture.presentation.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource

import com.kabindra.clean.architecture.R

@Composable
fun AppIcon(
    modifier: Modifier = Modifier
        .width(dimensionResource(id = com.intuit.sdp.R.dimen._250sdp))
        .height(dimensionResource(id = com.intuit.sdp.R.dimen._250sdp))
) {
    ImageHandlerRes(
        modifier = modifier,
        image = R.drawable.splash_icon,
        contentDescription = "App Icon",
    )
}

@Composable
fun AppIconFilled(
    modifier: Modifier = Modifier
        .width(dimensionResource(id = com.intuit.sdp.R.dimen._163sdp))
        .height(dimensionResource(id = com.intuit.sdp.R.dimen._63sdp))
) {
    ImageHandlerRes(
        modifier = modifier,
        image = R.drawable.splash_icon,
        contentDescription = "App Icon",
    )
}

@Composable
fun AppBrandIcon(
    modifier: Modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight()
) {
    ImageHandlerRes(
        modifier = modifier,
        image = R.drawable.splash_icon,
        contentDescription = "Brand Icon",
    )
}