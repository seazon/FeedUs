package com.seazon.feedus.ui.customize

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun FmOutlinedTextField(
    text: String? = null,
    placeHolder: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (newValue: String) -> Unit = {},
) {
    var value by remember {
        mutableStateOf(text.orEmpty())
    }
    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth(),
        trailingIcon = {
            if (enabled && value.isNotEmpty()) {
                Image(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "clear",
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .clickable { value = "" },
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = if (keyboardOptions.keyboardType != KeyboardType.Password) VisualTransformation.None else PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        ),
        placeholder = {
            Text(
                text = placeHolder.orEmpty(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
    )
}

@Preview
@Composable
fun FmOutlinedTextFieldPreview() {
    FmOutlinedTextField("dddd", "placeHolder")
}