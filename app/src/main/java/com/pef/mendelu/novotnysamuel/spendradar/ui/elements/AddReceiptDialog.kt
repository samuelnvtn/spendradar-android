package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor

@Composable
fun AddReceiptDialog(
    onDismissRequest: () -> Unit,
    navigation: INavigationRouter
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = barButton,
            border = BorderStroke(1.dp, barColor),
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_receipt),
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
                )

                OptionButton(
                    painter = painterResource(id = R.drawable.camera),
                    label = stringResource(R.string.scan_camera),
                    onClick = {
                        onDismissRequest()
                        // TODO: Camera logic
                    }
                )
/*
                OptionButton(
                    painter = painterResource(id = R.drawable.insert_photo),
                    label = stringResource(R.string.scan_gallery),
                    onClick = {
                        onDismissRequest()
                        // TODO: Gallery logic
                    }
                )
*/
                OptionButton(
                    painter = painterResource(id = R.drawable.edit),
                    label = stringResource(R.string.manual_entry),
                    onClick = {
                        onDismissRequest()
                        navigation.navigateToAdd()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.cancel), color = textColor)
                }
            }
        }
    }
}