package com.baccaro.kmp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.baccaro.kmp.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@AndroidApp)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}