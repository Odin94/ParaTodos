package c.odin.paratodos.activity.extensions

import android.app.Activity
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.DatePicker
import java.util.*


fun Calendar.openDatePicker(
    activity: Activity,
    onComplete: (view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) -> Unit
) {
    val year = get(Calendar.YEAR)
    val month = get(Calendar.MONTH)
    val day = get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        activity,
        DatePickerDialog.OnDateSetListener(onComplete),
        year,
        month,
        day
    )

    dpd.show()
}

fun Calendar.getDateString(format: String = "dd.MM.yyyy"): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)

fun Calendar.setDate(year: Int, month: Int, day: Int) {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, day)
}