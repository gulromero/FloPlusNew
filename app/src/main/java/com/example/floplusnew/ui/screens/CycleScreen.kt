package com.example.floplusnew.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.floplusnew.data.VitaminLogManager
import com.example.floplusnew.viewmodel.CycleViewModel
import com.example.floplusnew.model.getTipsForPhase
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CycleScreen(viewModel: CycleViewModel) {
    val context = LocalContext.current
    val vitaminLogManager = remember { VitaminLogManager(context.applicationContext) }
    val loggedToday by vitaminLogManager.vitaminLogFlow.collectAsState(initial = false)
    val scope = rememberCoroutineScope()
    var showLog by remember { mutableStateOf(false) }
    var streak by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        streak = vitaminLogManager.getCurrentStreak()
    }

    val selectedDate by viewModel.cycleStartDate.collectAsState()
    val currentPhase = remember(selectedDate) {
        viewModel.getCurrentPhase()
    }
    val tips = currentPhase?.let { getTipsForPhase(it) }

    if (showLog) {
        VitaminLogHistoryScreen(onBack = { showLog = false })
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = selectedDate?.let { "Cycle start: $it" } ?: "No cycle date set.",
            style = MaterialTheme.typography.headlineSmall
        )

        Text("Streak: $streak day${if (streak == 1) "" else "s"} in a row!")

        Button(onClick = {
            val today = LocalDate.now()
            val picker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val picked = LocalDate.of(year, month + 1, dayOfMonth)
                    viewModel.saveCycleStartDate(picked)
                },
                today.year,
                today.monthValue - 1,
                today.dayOfMonth
            )
            picker.show()
        }) {
            Text("Set Cycle Start Date")
        }

        currentPhase?.let {
            Text("Current Phase: $it", style = MaterialTheme.typography.titleLarge)

            tips?.let {
                Text("Vitamins: ${it.vitamins.joinToString(", ")}")
                Text("Snacks: ${it.snacks.joinToString(", ")}")
                Text("Motivation: ${it.motivation}")
            }

            Button(
                onClick = { showLog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View My Vitamin Log")
            }

            if (!loggedToday) {
                Button(
                    onClick = {
                        scope.launch {
                            vitaminLogManager.logToday()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("I took my vitamins today")
                }
            } else {
                Text("You've already logged today's vitamins!", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

}
