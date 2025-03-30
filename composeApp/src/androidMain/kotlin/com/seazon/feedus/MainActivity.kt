package com.seazon.feedus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.seazon.feedus.ui.login.LoginViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel by inject<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.onOAuth(intent.dataString)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}