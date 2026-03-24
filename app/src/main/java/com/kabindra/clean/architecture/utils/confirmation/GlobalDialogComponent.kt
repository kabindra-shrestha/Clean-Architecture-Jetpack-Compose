package com.kabindra.clean.architecture.utils.confirmation

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun GlobalDialogComponent(
    isVisible: Boolean,
    onDismissRequest: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    if (isVisible) {
        Dialog(onDismissRequest = onDismissRequest) {
            content()
            /*Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.sdp),
                shape = RoundedCornerShape(16.sdp),
            ) {
                content()
            }*/
        }
    }
}

