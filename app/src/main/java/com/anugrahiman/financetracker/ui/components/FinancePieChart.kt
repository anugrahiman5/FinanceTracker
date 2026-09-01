package com.anugrahiman.financetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun FinancePieChart(income: Double, expense: Double, modifier: Modifier = Modifier) {
    val total = income + expense

    // Kalkulasi matematika untuk sudut lingkaran (maksimal 360 derajat)
    val incomeSweep = if (total > 0) (income / total * 360).toFloat() else 180f
    val expenseSweep = if (total > 0) (expense / total * 360).toFloat() else 180f

    Canvas(modifier = modifier.size(180.dp)) {
        val strokeWidth = 35f

        // Menggunakan scope internal DrawScope bawaan Canvas
        val innerRadiusSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeftOffset = Offset(strokeWidth / 2, strokeWidth / 2)

        // 1. Gambar Busur Pemasukan (Hijau)
        drawArc(
            color = Color(0xFF2ECC71),
            startAngle = -90f,
            sweepAngle = incomeSweep,
            useCenter = false,
            style = Stroke(width = strokeWidth),
            size = innerRadiusSize,
            topLeft = topLeftOffset
        )

        // 2. Gambar Busur Pengeluaran (Merah)
        drawArc(
            color = Color(0xFFE74C3C),
            startAngle = -90f + incomeSweep,
            sweepAngle = expenseSweep,
            useCenter = false,
            style = Stroke(width = strokeWidth),
            size = innerRadiusSize,
            topLeft = topLeftOffset
        )
    }
}
