package com.kabindra.inappupdate.ui.component

import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource

@Composable
fun ButtonNormal(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .requiredWidth(dimensionResource(id = com.intuit.sdp.R.dimen._120sdp)),
        enabled = enabled,
        onClick = onClick
    ) {
        TextButtonAction(text = text)
    }
}