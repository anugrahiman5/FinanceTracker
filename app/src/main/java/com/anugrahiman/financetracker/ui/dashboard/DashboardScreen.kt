package com.anugrahiman.financetracker.ui.dashboard

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.anugrahiman.financetracker.data.local.TransactionEntity
import com.anugrahiman.financetracker.ui.components.FinancePieChart
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val balanceState by viewModel.balanceState.collectAsState()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    // Scaffolding membantu memberikan skema latar belakang warna otomatis yang mendukung Dark Mode
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Finance Tracker",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Tampilan Grafik Visualisasi Canvas Custom
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                FinancePieChart(
                    income = balanceState.totalIncome,
                    expense = balanceState.totalExpense
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Ringkasan Saldo Keuangan Berbentuk Card Adaptif
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Total Saldo: Rp ${balanceState.totalBalance}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pemasukan: Rp ${balanceState.totalIncome}", color = Color(0xFF2ECC71))
                    Text("Pengeluaran: Rp ${balanceState.totalExpense}", color = Color(0xFFE74C3C))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Form Input Data Keuangan Lokal dengan Pewarnaan Terkontrol
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Nama Transaksi") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Jumlah (Rp)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull() ?: 0.0
                        if (title.isNotEmpty() && amt > 0) {
                            viewModel.addTransaction(title, amt, true)
                            title = ""; amount = ""
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
                ) { Text("Pemasukan") }

                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull() ?: 0.0
                        if (title.isNotEmpty() && amt > 0) {
                            viewModel.addTransaction(title, amt, false)
                            title = ""; amount = ""
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                ) { Text("Pengeluaran") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Tombol Fitur Ekspor File CSV
            Button(
                onClick = {
                    val file = exportToCSV(context, transactions)
                    if (file != null) {
                        Toast.makeText(context, "Laporan berhasil diekspor!", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            "Gagal mengekspor laporan (Data masih kosong)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ekspor Laporan ke CSV")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Tombol Fitur Share File CSV
            Button(
                onClick = {
                    // Mengambil referensi file yang ada di memori internal
                    val file = File(context.filesDir, "Laporan_Keuangan_Tracker.csv")
                    if (file.exists()) {
                        shareCSVFile(context, file) // Panggil fungsi share jika file ada
                    } else {
                        Toast.makeText(
                            context,
                            "Silakan klik 'Ekspor Laporan' terlebih dahulu!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Bagikan Laporan CSV")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Riwayat Transaksi",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Daftar Aliran Data Riwayat Finansial (LazyColumn)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(transactions) { item ->
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background // List item menyatu dengan background
                        ),
                        headlineContent = {
                            Text(
                                item.title,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        supportingContent = {
                            Text(
                                if (item.isIncome) "Masuk" else "Keluar",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        },
                        trailingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${if (item.isIncome) "+" else "-"} Rp ${item.amount}",
                                    color = if (item.isIncome) Color(0xFF2ECC71) else Color(
                                        0xFFE74C3C
                                    ),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                IconButton(onClick = { viewModel.deleteTransaction(item) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Hapus Transaksi",
                                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f) // Icon adaptif
                                    )
                                }
                            }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}

// Fungsi Internal Manipulasi File untuk Konversi Data Room ke Format Dokumen CSV
fun exportToCSV(context: Context, transactions: List<TransactionEntity>): File? {
    if (transactions.isEmpty()) return null

    val file = File(context.filesDir, "Laporan_Keuangan_Tracker.csv")

    return try {
        val writer = FileWriter(file)
        // Menulis Header kolom CSV
        writer.append("ID;Nama Transaksi;Jumlah Uang;Jenis Transaksi;Tanggal\n")

        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        // 3. Menulis Baris Data Keuangan
        for (item in transactions) {
            val type = if (item.isIncome) "Pemasukan" else "Pengeluaran"
            val dateString = dateFormat.format(Date(item.timestamp))
            writer.append("${item.id};${item.title};${item.amount};$type;$dateString\n")
        }

        writer.flush()
        writer.close()
        file // Berhasil mengembalikan file
    } catch (e: Exception) {
        e.printStackTrace()
        null // Gagal membuat file
    }
}

fun shareCSVFile(context: Context, file: File) {
    // Mengubah file mentah menjadi URI aman menggunakan FileProvider yang didaftarkan di Manifest
    val uri = FileProvider.getUriForFile(
        context,
        "com.anugrahiman.financetracker.fileprovider",
        file
    )

    // Membuat Intent Share bawaan Android
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Memberikan izin baca darurat ke aplikasi luar
    }

    // Membuka jendela pilihan aplikasi (WhatsApp, Drive, Gmail, dll)
    context.startActivity(Intent.createChooser(intent, "Bagikan Laporan Keuangan Melalui:"))
}
