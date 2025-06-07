package com.example.floplusnew.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.floplusnew.data.VitaminLogManager

@Composable
fun VitaminLogHistoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val manager = remember { VitaminLogManager(context.applicationContext) }
    val allDates by manager.allLoggedDatesFlow.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🌿 My Vitamin Log", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = onBack) {
            Text("⬅ Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (allDates.isEmpty()) {
            Text("No vitamins logged yet!")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(allDates) { date ->
                    Text("✅ $date", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
