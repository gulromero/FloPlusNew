package com.example.floplusnew.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.floplusnew.R
import com.example.floplusnew.data.VitaminLogManager

@Composable
fun VitaminLogHistoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val manager = remember { VitaminLogManager(context.applicationContext) }
    val allDates by manager.allLoggedDatesFlow.collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cycle_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFD64896)
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("⬅ Back")
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                "🌿 My Vitamin Log",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (allDates.isEmpty()) {
                Text(
                    "No vitamins logged yet!",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(allDates) { date ->
                        Text(
                            "✅ $date",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                        )
                    }
                }
            }
        }
    }
}
