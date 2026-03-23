package com.kabindra.clean.architecture.utils.error

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kabindra.clean.architecture.R
import com.kabindra.clean.architecture.presentation.ui.component.ButtonText
import com.kabindra.clean.architecture.presentation.ui.component.ImageHandlerLottie
import com.kabindra.clean.architecture.presentation.ui.component.TextComponent
import com.kabindra.clean.architecture.utils.constants.StatusCode.Companion.STATUS_CODE_NOT_HANDLED
import com.kabindra.clean.architecture.utils.handler.HandleResponseStatusCode

@Composable
fun GlobalErrorDialog(
    isVisible: Boolean = false,
    isAction: Boolean = false,
    statusCode: Int = -1,
    title: String,
    message: String,
    onDismiss: () -> Unit = {},
    onNavigateLogin: () -> Unit = {}
) {
    val openDialog = remember { mutableStateOf(isVisible) }

    if (openDialog.value) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.error
            )
        )

        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp)),
                shape = RoundedCornerShape(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)),
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ImageHandlerLottie(
                        modifier = Modifier
                            .width(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                            .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)),
                        image = composition,
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)))
                    TextComponent(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)))
                    TextComponent(
                        modifier = Modifier.fillMaxWidth(),
                        text = message,
                        textAlign = TextAlign.Center,
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)))
                    if (isAction) {
                        Row(
                            modifier = Modifier
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ButtonText(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)),
                                text = "Ok",
                                onClick = {
                                    openDialog.value = false

                                    val statusCodeHandler = HandleResponseStatusCode(
                                        statusCode,
                                        onNavigateLogin = { onNavigateLogin() }
                                    ).statusCodeHandler()

                                    if (statusCodeHandler == STATUS_CODE_NOT_HANDLED) {
                                        onDismiss()
                                    }
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
fun ServerMaintenanceDialog(
    isVisible: Boolean = false,
    isAction: Boolean = false,
    title: String,
    message: String,
    onDismiss: () -> Unit = {}
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
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp)),
                shape = RoundedCornerShape(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)),
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ImageHandlerLottie(
                        modifier = Modifier
                            .width(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                            .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)),
                        image = composition,
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)))
                    TextComponent(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)))
                    TextComponent(
                        modifier = Modifier.fillMaxWidth(),
                        text = message,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)))
                    if (isAction) {
                        Row(
                            modifier = Modifier
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ButtonText(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)),
                                text = "Ok",
                                onClick = {
                                    openDialog.value = false

                                    onDismiss()
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
fun VersionCheckDialog(
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
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp)),
                shape = RoundedCornerShape(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)),
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ImageHandlerLottie(
                        modifier = Modifier
                            .width(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp))
                            .height(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)),
                        image = composition,
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)))
                    TextComponent(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._4sdp)))
                    TextComponent(
                        modifier = Modifier.fillMaxWidth(),
                        text = message,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)))
                    if (isAction) {
                        Row(
                            modifier = Modifier
                                .padding(dimensionResource(id = com.intuit.sdp.R.dimen._2sdp))
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ButtonText(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)),
                                text = "Update",
                                onClick = {
                                    openDialog.value = false

                                    onUpdate()
                                }
                            )
                            if (!isForceUpdate) {
                                ButtonText(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .width(dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)),
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