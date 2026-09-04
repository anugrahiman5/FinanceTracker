package com.anugrahiman.financetracker.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun FinancePieChart(income: Double, expense: Double, modifier: Modifier = Modifier) {
    val total = income + expense

    // Status untuk memicu jalannya animasi saat pertama kali masuk layar
    var animationPlayed by remember { mutableStateOf(false) }

    // Faktor pengali animasi dari 0.0 (kosong) sampai 1.0 (penuh) selama 1000 milidetik (1 detik)
    val animationProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1200,
        easing = FastOutSlowInEasing
        )
    )

    // Efek samping untuk mengaktifkan animasi begitu komponen muncul di layar
    LaunchedEffect(key1 = total) {
        animationPlayed = false
        animationPlayed = true
    }

    // Kalkulasi matematika untuk sudut lingkaran (maksimal 360 derajat)
    val targetIncomeSweep = if (total > 0) (income / total * 360).toFloat() else 180f
    val targetExpenseSweep = if (total > 0) (expense / total * 360).toFloat() else 180f

    // Sudut dinamis yang dikalikan dengan nilai kemajuan animasi secara realtime
    val currentIncomeSweep = targetIncomeSweep * animationProgress
    val currentExpenseSweep = targetExpenseSweep * animationProgress
    Canvas(modifier = modifier.size(180.dp)) {
        val strokeWidth = 35f

        // Menggunakan scope internal DrawScope bawaan Canvas
        val innerRadiusSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeftOffset = Offset(strokeWidth / 2, strokeWidth / 2)

        // 1. Gambar Busur Pemasukan (Hijau)
        drawArc(
            color = Color(0xFF2ECC71),
            startAngle = -90f,
            sweepAngle = currentIncomeSweep,
            useCenter = false,
            style = Stroke(width = strokeWidth),
            size = innerRadiusSize,
            topLeft = topLeftOffset
        )

        // 2. Gambar Busur Pengeluaran (Merah)
        drawArc(
            color = Color(0xFFE74C3C),
            startAngle = -90f + currentIncomeSweep,
            sweepAngle = currentExpenseSweep,
            useCenter = false,
            style = Stroke(width = strokeWidth),
            size = innerRadiusSize,
            topLeft = topLeftOffset
        )
    }
}
