package c.odin.paratodos.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Todo(
    val id: Int,
    val date_created: String,
    var title: String,
    var description: String,
    var priority: String,
    var date_reminder: String,
    var date_due: String
) : Parcelable