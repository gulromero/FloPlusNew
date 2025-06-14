package com.example.floplusnew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.floplusnew.data.CycleStartDateManager
import com.example.floplusnew.network.CycleBot
import com.example.floplusnew.network.ChatRequest
import com.example.floplusnew.network.Message
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class CycleViewModel(
    private val cycleStartDateManager: CycleStartDateManager
) : ViewModel() {

    val cycleStartDate: StateFlow<LocalDate?> =
        cycleStartDateManager.cycleStartDateFlow
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    fun saveCycleStartDate(date: LocalDate) {
        viewModelScope.launch {
            cycleStartDateManager.saveCycleStartDate(date)
        }
    }

    fun getCurrentPhase(): CyclePhase? {
        val startDate = cycleStartDate.value ?: return null
        val daysSinceStart = LocalDate.now().toEpochDay() - startDate.toEpochDay()
        val dayInCycle = (daysSinceStart % 28).toInt()

        return when (dayInCycle) {
            in 0..4 -> CyclePhase.MENSTRUAL
            in 5..13 -> CyclePhase.FOLLICULAR
            14 -> CyclePhase.OVULATION
            in 15..27 -> CyclePhase.LUTEAL
            else -> null
        }
    }


    fun clearCycleStartDate() {
        viewModelScope.launch {
            cycleStartDateManager.clearCycleStartDate()
        }
    }

}

enum class CyclePhase {
    MENSTRUAL, FOLLICULAR, OVULATION, LUTEAL
}
