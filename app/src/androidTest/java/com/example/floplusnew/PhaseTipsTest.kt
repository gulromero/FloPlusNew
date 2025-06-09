package com.example.floplusnew
import com.example.floplusnew.model.getTipsForPhase
import com.example.floplusnew.viewmodel.CyclePhase
import org.junit.Assert.*
import org.junit.Test

class PhaseTipsTest {

    @Test
    fun testGetTipsForPhase_luteal_containsMagnesium() {
        val tips = getTipsForPhase(CyclePhase.LUTEAL)
        assertTrue(tips.vitamins.contains("Magnesium"))
    }

}