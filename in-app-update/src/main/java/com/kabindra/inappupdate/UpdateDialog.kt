package com.kabindra.inappupdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kabindra.inappupdate.ui.component.ButtonNormal
import com.kabindra.inappupdate.ui.component.ImageHandlerLottie
import com.kabindra.inappupdate.ui.component.TextMedium
import com.kabindra.inappupdate.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun UpdateDownloadDialog(
    isVisible: Boolean = false,
    isAction: Boolean = false,
    message: String,
    onDismiss: () -> Unit = {},
    onInstall: () -> Unit = {}
) {
    val openDialog = remember { mutableStateOf(isVisible) }

    if (!isAction) {
        // Automatically dismiss the dialog after 5 seconds
        LaunchedEffect(openDialog.value) {
            if (openDialog.value) {
                delay(5000) // 5 seconds delay

                openDialog.value = false

                onDismiss()
            }
        }
    }

    if (openDialog.value) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.success
            )
        )
        val progress by animateLottieCompositionAsState(composition)

        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.paddingSmall),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(AppTheme.dimens.paddingSmall)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ImageHandlerLottie(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        image = composition,
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextMedium(
                        text = message,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isAction) {
                        Row(
                            modifier = Modifier
                                .padding(AppTheme.dimens.paddingSmall)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ButtonNormal(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(100.dp),
                                text = "Install",
                                onClick = {
                                    openDialog.value = false

                                    onInstall()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateAvailableDialog(
    isVisible: Boolean = false,
    isAction: Boolean = false,
    isForceUpdate: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit = {},
    onUpdate: () -> Unit = {},
    onLater: () -> Unit = {}
) {
    val openDialog = remember { mutableStateOf(isVisible) }

    if (openDialog.value) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.warning
            )
        )

        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.paddingSmall),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(AppTheme.dimens.paddingSmall)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ImageHandlerLottie(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        image = composition,
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextMedium(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TextMedium(
                        modifier = Modifier.fillMaxWidth(),
                        text = message,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isAction) {
                        Row(
                            modifier = Modifier
                                .padding(AppTheme.dimens.paddingSmall)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ButtonNormal(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(100.dp),
                                text = "Update",
                                onClick = {
                                    openDialog.value = false

                                    onUpdate()
                                }
                            )
                            if (!isForceUpdate) {
                                ButtonNormal(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .width(100.dp),
                                    text = "Later",
                                    onClick = {
                                        openDialog.value = false

                                        onLater()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}