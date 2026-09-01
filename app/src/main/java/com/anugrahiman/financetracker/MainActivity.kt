package com.anugrahiman.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahiman.financetracker.data.local.ExpenseDatabase
import com.anugrahiman.financetracker.data.repository.TransactionRepository
import com.anugrahiman.financetracker.ui.dashboard.DashboardScreen
import com.anugrahiman.financetracker.ui.dashboard.DashboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi Database dan Repository Lokal
        val database = ExpenseDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())

        // 2. Membuat ViewModel menggunakan Factory Pattern yang aman
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    return DashboardViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val viewModel = ViewModelProvider(this, viewModelFactory)[DashboardViewModel::class.java]

        // 3. Menampilkan Konten Utama dengan Pembungkus Tema Otomatis
        setContent {
            CustomFinanceTheme {
                DashboardScreen(viewModel = viewModel)
            }
        }
    }
}

// =======================================================================
// FUNGSI TEMA CUSTOM UNTUK MENDETEKSI DARK MODE OTOMATIS PADA JETPACK COMPOSE
// =======================================================================
@Composable
fun CustomFinanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Otomatis membaca status Dark Mode dari handphone
    content: @Composable () -> Unit
) {
    // Warna untuk Mode Terang
    val lightColors = lightColorScheme(
        primary = Color(0xFF2ECC71),
        background = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF0F3F4),
        onBackground = Color(0xFF1C1B1F),
        onSurfaceVariant = Color(0xFF1C1B1F)
    )

    // Warna untuk Mode Gelap (Dark Mode)
    val darkColors = darkColorScheme(
        primary = Color(0xFF2ECC71),
        background = Color(0xFF121212),
        surfaceVariant = Color(0xFF1E1E1E),
        onBackground = Color(0xFFFFFFFF),
        onSurfaceVariant = Color(0xFFFFFFFF)
    )

    val colors = if (darkTheme) darkColors else lightColors
    val view = LocalView.current

    // WARNA DI STATUS BAR
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as ComponentActivity).window

            // 1. Set warna latar belakang status bar mengikuti warna background aplikasi
            window.statusBarColor = colors.background.toArgb()

            // 2. Kontrol warna ikon & teks (jam, baterai, notifikasi) di atasnya
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
