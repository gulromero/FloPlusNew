package com.example.floplusnew.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import kotlin.collections.map

class VitaminLogManager(private val context: Context) {

    companion object {
        private val VITAMIN_LOG_SET = stringSetPreferencesKey("vitamin_log_dates")
    }

    suspend fun logToday() {
        val today = LocalDate.now().toString()
        context.dataStore.edit { prefs ->
            val current = prefs[VITAMIN_LOG_SET]?.toMutableSet() ?: mutableSetOf()
            current.add(today)
            prefs[VITAMIN_LOG_SET] = current
        }
    }

    val vitaminLogFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            val set = prefs[VITAMIN_LOG_SET]
            set?.contains(LocalDate.now().toString()) == true
        }

    val allLoggedDatesFlow: Flow<List<String>> = context.dataStore.data
        .map { prefs ->
            prefs[VITAMIN_LOG_SET]?.toList()?.sortedDescending() ?: emptyList()
        }


    suspend fun getCurrentStreak(): Int {
        val prefs = context.dataStore.data.first()
        val dates = prefs[VITAMIN_LOG_SET]?.map { LocalDate.parse(it) }?.sortedDescending() ?: return 0

        var streak = 0
        var current = LocalDate.now()

        for (date in dates) {
            if (date == current) {
                streak++
                current = current.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }

}