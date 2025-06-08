package com.example.floplusnew

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.floplusnew.data.CycleStartDateManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class CycleStartDateManagerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val manager = CycleStartDateManager(context)

    @Test
    fun testSaveAndRetrieveCycleStartDate() = runBlocking {
        val testDate = LocalDate.of(2025, 6, 1)
        manager.saveCycleStartDate(testDate)
        val storedDate = manager.cycleStartDateFlow.first()
        assertEquals(testDate, storedDate)
    }
}