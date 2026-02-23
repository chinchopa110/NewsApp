package com.example.newsapp.domain.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))
    private val shortDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    
    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
    
    fun formatShortDate(date: Date): String {
        return shortDateFormat.format(date)
    }
    
    fun getTimeAgo(date: Date): String {
        val now = Date()
        val diffInMillis = now.time - date.time
        
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        
        return when {
            seconds < 60 -> "только что"
            minutes < 60 -> "$minutes ${getPluralForm(minutes.toInt(), "минуту", "минуты", "минут")} назад"
            hours < 24 -> "$hours ${getPluralForm(hours.toInt(), "час", "часа", "часов")} назад"
            days < 7 -> "$days ${getPluralForm(days.toInt(), "день", "дня", "дней")} назад"
            days < 30 -> "${days / 7} ${getPluralForm((days / 7).toInt(), "неделю", "недели", "недель")} назад"
            days < 365 -> "${days / 30} ${getPluralForm((days / 30).toInt(), "месяц", "месяца", "месяцев")} назад"
            else -> "${days / 365} ${getPluralForm((days / 365).toInt(), "год", "года", "лет")} назад"
        }
    }
    
    private fun getPluralForm(number: Int, form1: String, form2: String, form5: String): String {
        val n = number % 100
        val n1 = number % 10
        
        return when {
            n in 11..19 -> form5
            n1 == 1 -> form1
            n1 in 2..4 -> form2
            else -> form5
        }
    }
    
    fun isToday(date: Date): Boolean {
        val calendar1 = Calendar.getInstance().apply { time = date }
        val calendar2 = Calendar.getInstance()
        
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
               calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }
    
    fun isPast(date: Date): Boolean {
        return date.before(Date())
    }
    
    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }
}

fun Date.formatToString(): String {
    return DateUtils.formatDate(this)
}

fun Date.toTimeAgo(): String {
    return DateUtils.getTimeAgo(this)
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this)
}