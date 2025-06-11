package com.example.floplusnew.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.floplusnew.R
import com.example.floplusnew.data.VitaminLogManager
import com.example.floplusnew.model.getTipsForPhase
import com.example.floplusnew.viewmodel.AuthViewModel
import com.example.floplusnew.viewmodel.CycleViewModel
import com.example.floplusnew.network.ChatRequest
import com.example.floplusnew.network.Message
import com.example.floplusnew.network.CycleBot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlinx.coroutines.GlobalScope

@Composable
fun CycleScreen(
    viewModel: CycleViewModel,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
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
    val currentPhase = viewModel.getCurrentPhase()

    val tips = currentPhase?.let { getTipsForPhase(it) }
    var userQuestion by remember { mutableStateOf("") }
    val cycleBotReply by CycleBotHandler.chatbotResponse.collectAsState()

    if (showLog) {
        VitaminLogHistoryScreen(onBack = { showLog = false })
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cycle_bg),
            contentDescription = "Cycle Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = selectedDate?.let { "Cycle start: $it" } ?: "No cycle date set.",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    "Streak: $streak day${if (streak == 1) "" else "s"} in a row!",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val today = LocalDate.now()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val picked = LocalDate.of(year, month + 1, dayOfMonth)
                                viewModel.saveCycleStartDate(picked)
                            },
                            today.year, today.monthValue - 1, today.dayOfMonth
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFD64896)
                    ),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp)
                ) {
                    Text("Log Period", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(21.dp))
                Button(
                    onClick = { viewModel.clearCycleStartDate() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFD64896)
                    ),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp)
                ) {
                    Text("Clear", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(21.dp))

                Button(
                    onClick = {
                        authViewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFD64896)
                    ),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp)
                ) {
                    Text("LOGOUT", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            currentPhase?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Current Phase: $it",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    tips?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Vitamins to focus on today: ${it.vitamins.joinToString(", ")}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                            )
                            Text(
                                text = "Snacks: ${it.snacks.joinToString(", ")}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        val scriptFont = FontFamily(Font(R.font.mouldycheese_font))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.motivation,
                                color = Color.White,
                                fontSize = 30.sp,
                                fontFamily = scriptFont,
                                lineHeight = 32.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                currentPhase?.let {
                    Button(
                        onClick = { showLog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFFD64896)
                        ),
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .width(220.dp)
                            .height(50.dp)
                    ) {
                        Text("View My Vitamin Log", fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!loggedToday) {
                        Button(
                            onClick = {
                                scope.launch {
                                    vitaminLogManager.logToday()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFD64896)
                            ),
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier
                                .width(220.dp)
                                .height(50.dp)
                        ) {
                            Text("I took my vitamins today", fontWeight = FontWeight.Medium)
                        }
                    } else {
                        Text(
                            "You've already logged today's vitamins!",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
        }

        Divider()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                "💬 Ask CycleBot:",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = userQuestion,
                onValueChange = { userQuestion = it },
                label = { Text("Ask anything about your cycle...") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD64896),
                    cursorColor = Color(0xFFD64896),
                    focusedLabelColor = Color(0xFFD64896)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    CycleBotHandler.askCycleBot(userQuestion)
                    userQuestion = ""
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD64896),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Send")
            }

            if (cycleBotReply.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "CycleBot says:\n$cycleBotReply",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
            }
        }
    }
}
private object CycleBotHandler {
    private val apiKey = "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"
    private val cycleBot = CycleBot.create(apiKey)

    private val _chatbotResponse = MutableStateFlow("")
    val chatbotResponse: StateFlow<String> = _chatbotResponse

    fun askCycleBot(question: String) {
        GlobalScope.launch {
            try {
                val response = cycleBot.sendMessage(
                    ChatRequest(
                        messages = listOf(
                            Message("system", "You are CycleBot, a sweet and supportive friend who answers all menstruation-related questions with love, facts, and emojis."),
                            Message("user", question)
                        )
                    )
                )
                _chatbotResponse.value = response.choices.firstOrNull()?.message?.content
                    ?: "CycleBot's not available right now.."
            } catch (e: Exception) {
                _chatbotResponse.value = "Something went wrong: ${e.localizedMessage}"
            }
        }
    }
}


