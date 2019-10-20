package c.odin.paratodos.activity.extensions

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.*


fun Calendar.getDateString(): String {
    return SimpleDateFormat(
        "dd.MM.yyyy",
        Locale.getDefault()
    ).format(Calendar.getInstance())
}

fun Calendar.setDate(year: Int, month: Int, day: Int) {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, day)
}