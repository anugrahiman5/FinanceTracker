package com.anugrahiman.financetracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahiman.financetracker.data.local.TransactionEntity
import com.anugrahiman.financetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: TransactionRepository): ViewModel() {
    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val balanceState: StateFlow<BalanceUiState> = transactions.map { list ->
        val income = list.filter { it.isIncome }.sumOf { it.amount }
        val expense = list.filter { !it.isIncome }.sumOf { it.amount }
        BalanceUiState(
            totalBalance = income - expense,
            totalIncome = income,
            totalExpense = expense
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BalanceUiState())

    fun addTransaction(title: String, amount: Double, isIncome: Boolean){
        viewModelScope.launch { repository.insert(TransactionEntity(title = title, amount = amount, isIncome = isIncome)) }
    }

    fun deleteTransaction(transaction: TransactionEntity){
        viewModelScope.launch { repository.delete(transaction) }
    }
}

data class BalanceUiState(
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
)