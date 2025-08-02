package com.kabindra.clean.architecture.utils.confirmation

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kabindra.clean.architecture.R
import com.kabindra.clean.architecture.presentation.ui.component.ButtonText
import com.kabindra.clean.architecture.presentation.ui.component.ImageHandlerLottie
import com.kabindra.clean.architecture.presentation.ui.component.TextComponent
import com.kabindra.clean.architecture.presentation.ui.theme.AppTheme

@Composable
fun GlobalConfirmationDialog(
    isVisible: Boolean = false,
    isAction: Boolean = false,
    message: String,
    onCancel: () -> Unit = {},
    onContinue: () -> Unit = {}
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
                    TextComponent(
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
                            ButtonText(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(110.dp),
                                text = "Cancel",
                                onClick = {
                                    openDialog.value = false

                                    onCancel()
                                }
                            )
                            ButtonText(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(110.dp),
                                text = "Continue",
                                onClick = {
                                    openDialog.value = false

                                    onContinue()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}