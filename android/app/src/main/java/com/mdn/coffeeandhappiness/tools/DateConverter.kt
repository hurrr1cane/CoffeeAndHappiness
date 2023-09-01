package com.mdn.coffeeandhappiness.tools

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverter {
    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val date: Date = inputFormat.parse(inputDate) ?: Date()

        return outputFormat.format(date)
    }
}