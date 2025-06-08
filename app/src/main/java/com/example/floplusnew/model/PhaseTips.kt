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
            motivation = "Your period isn’t a weakness — it’s a monthly reminder that you’re magic, messy and magnificent 🩸"
        )
        CyclePhase.FOLLICULAR -> PhaseTips(
            vitamins = listOf("Vitamin D", "B12", "Zinc"),
            snacks = listOf("Greek yogurt", "Boiled eggs", "Pumpkin seeds"),
            motivation = "You’re entering your follicular phase — energy’s creeping back and your brain's feeling cute again. Go chase dreams, goals, and own it all. ✨ "
        )
        CyclePhase.OVULATION -> PhaseTips(
            vitamins = listOf("Vitamin E", "Omega-3", "B6"),
            snacks = listOf("Avocado toast", "Salmon bites", "Walnuts"),
            motivation = "Ovulation unlocked: confidence is maxed, energy is poppin’, and you’re basically unstoppable. Go own it! "
        )
        CyclePhase.LUTEAL -> PhaseTips(
            vitamins = listOf("Calcium", "Magnesium", "Vitamin B6"),
            snacks = listOf("Banana", "Oats with honey", "Dark leafy greens"),
            motivation = "This is your luteal phase, babe — tired? Moody? Craving everything? Totally normal. Know you’re STILL that girl. Just slower and softer today. 🌘💤"
        )
    }
}
