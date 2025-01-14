package com.baccaro.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.baccaro.kmp.di.initializeKoin

fun MainViewController(
) = ComposeUIViewController(configure = { initializeKoin() }) {
    App()
}