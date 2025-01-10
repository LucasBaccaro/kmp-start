package com.baccaro.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.baccaro.kmp.di.initializeKoin
import platform.UIKit.UIViewController

fun MainViewController(
) = ComposeUIViewController(configure = { initializeKoin() }) {
    App()
}

lateinit var mapViewController: () -> UIViewController