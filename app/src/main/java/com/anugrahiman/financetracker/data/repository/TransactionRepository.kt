package com.anugrahiman.financetracker.data.repository

import com.anugrahiman.financetracker.data.local.TransactionDao
import com.anugrahiman.financetracker.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    // Mengambil semua data transaksi dari DAO berupa aliran data realtime (Flow)
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    // Menambah atau memperbarui transaksi melalui background thread (suspend)
    suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    // Menghapus transaksi melalui background thread (suspend)
    suspend fun delete(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }
}
