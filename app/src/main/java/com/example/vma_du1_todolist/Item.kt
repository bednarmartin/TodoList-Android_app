package com.example.vma_du1_todolist

import android.icu.text.SimpleDateFormat

data class Item(
    val title: String,
    val date: Long,
    var done: Boolean = false,
) {
    object Format {
        val dateTimeFormat = SimpleDateFormat("dd.MM.YYYY HH:mm")
        val dateFormat = SimpleDateFormat("dd.MM.YYYY")

        fun formatTime(hours: Int, minutes: Int): String {
            val newHours = when (hours) {
                in 0..9 -> "0${hours}"
                else -> hours.toString()
            }
            val newMinutes = when (minutes) {
                in 0..9 -> "0${minutes}"
                else -> minutes.toString()
            }
            return "$newHours:$newMinutes"
        }
    }
}