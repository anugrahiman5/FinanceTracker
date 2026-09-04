package com.anugrahiman.financetracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahiman.financetracker.data.local.ExpenseDatabase
import com.anugrahiman.financetracker.data.repository.TransactionRepository
import com.anugrahiman.financetracker.ui.dashboard.DashboardScreen
import com.anugrahiman.financetracker.ui.dashboard.DashboardViewModel
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ExpenseDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())

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

        // Setup sistem Biometrik Keamanan
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Jika pengguna membatalkan atau sensor error, tutup aplikasi demi keamanan data
                    Toast.makeText(applicationContext, "Autentikasi diperlukan untuk membuka data keuangan!", Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // JIKA SUKSES: Baru tampilkan halaman utama finansial
                    setContent {
                        CustomFinanceTheme {
                            DashboardScreen(viewModel = viewModel)
                        }
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Sidik jari tidak cocok!", Toast.LENGTH_SHORT).show()
                }
            })

        // Pengaturan teks informasi pada lembar dialog sidik jari
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Kunci Keamanan Finansial")
            .setSubtitle("Pindai sidik jari Anda untuk mengakses aplikasi")
            .setNegativeButtonText("Keluar")
            .build()

        // Pemicu otomatis dialog kunci muncul begitu aplikasi pertama kali dibuka
        biometricPrompt.authenticate(promptInfo)
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
