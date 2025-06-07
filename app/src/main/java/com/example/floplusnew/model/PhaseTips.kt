package com.example.floplusnew.model

import com.example.floplusnew.viewmodel.CyclePhase

data class PhaseTips(
    val vitamins: List<String>,
    val snacks: List<String>,
    val motivation: String
)

fun getTipsForPhase(phase: CyclePhase): PhaseTips {
    return when (phase) {
        CyclePhase.MENSTRUAL -> PhaseTips(
            vitamins = listOf("Iron", "Vitamin C", "Magnesium"),
            snacks = listOf("Dark chocolate", "Spinach smoothie", "Orange slices"),
            motivation = "Take it slow, your body is doing a lot. Rest, babe"
        )
        CyclePhase.FOLLICULAR -> PhaseTips(
            vitamins = listOf("Vitamin D", "B12", "Zinc"),
            snacks = listOf("Greek yogurt", "Boiled eggs", "Pumpkin seeds"),
            motivation = "You’re rising, hot stuff — go chase that dream "
        )
        CyclePhase.OVULATION -> PhaseTips(
            vitamins = listOf("Vitamin E", "Omega-3", "B6"),
            snacks = listOf("Avocado toast", "Salmon bites", "Walnuts"),
            motivation = "You’re magnetic rn — go flirt with life, babe "
        )
        CyclePhase.LUTEAL -> PhaseTips(
            vitamins = listOf("Calcium", "Magnesium", "Vitamin B6"),
            snacks = listOf("Banana", "Oats with honey", "Dark leafy greens"),
            motivation = "Mood swings? You’re still THAT girl. Breathe. "
        )
    }
}
