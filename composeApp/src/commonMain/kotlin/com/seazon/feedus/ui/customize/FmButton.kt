package com.seazon.feedus.ui.customize

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FmPrimaryButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit = {}) {
    Button(
        modifier = modifier
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        onClick = {
            onClick()
        }) {
        Text(
            text = text.uppercase(),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun FmOutlineButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit = {}) {
    OutlinedButton(
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .height(48.dp)
            .background(Color.Transparent),
        onClick = {
            onClick()
        }, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        )
    ) {
        Text(
            text = text.uppercase(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview
@Composable
fun FmPrimaryButtonLightPreview() {
    FmPrimaryButton(text = "你好Compose")
}

@Preview
@Composable
fun FmPrimaryButtonDarkPreview() {
    FmPrimaryButton(text = "你好Compose")
}

@Preview
@Composable
fun FmOutlineButtonLightPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        FmOutlineButton(text = "你好Compose")
    }
}

@Preview
@Composable
fun FmOutlineButtonDarkPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        FmOutlineButton(text = "你好Compose")
    }
}