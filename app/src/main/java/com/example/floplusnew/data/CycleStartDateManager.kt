package com.example.floplusnew.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate


val Context.dataStore by preferencesDataStore(name = "cycle_prefs")

class CycleStartDateManager(private val context: Context) {

    companion object {
        private val CYCLE_START_DATE = stringPreferencesKey("cycle_start_date")
    }

    suspend fun saveCycleStartDate(date: LocalDate) {
        val dateString = date.toString()
        context.dataStore.edit { prefs ->
            prefs[CYCLE_START_DATE] = dateString
        }
    }

    val cycleStartDateFlow: Flow<LocalDate?> = context.dataStore.data
        .map { prefs ->
            prefs[CYCLE_START_DATE]?.let { LocalDate.parse(it) }
        }
}
