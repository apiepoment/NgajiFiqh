package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppSettings
import com.example.data.AppSettingsDataStore
import com.example.data.AppThemeMode
import com.example.ui.theme.NgajiFiqihTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsStore = remember { AppSettingsDataStore(applicationContext) }
            val appSettings by settingsStore.settingsFlow.collectAsStateWithLifecycle(initialValue = AppSettings())
            val isDark = when (appSettings.themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }
            NgajiFiqihTheme(darkTheme = isDark) {
                NgajiFiqihApp(settingsStore = settingsStore)
            }
        }
    }
}
