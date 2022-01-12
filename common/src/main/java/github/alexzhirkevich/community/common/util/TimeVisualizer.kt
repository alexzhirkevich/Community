package github.alexzhirkevich.community.common.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import github.alexzhirkevich.community.common.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

val RUSSIAN : Locale
    get() = Locale("ru","RU")

class TimeVisualizer(private val context: Context,private var ms : Long) {

    companion object {
        const val HOUR : Long = 3_600_000
        const val DAY : Long = HOUR * 24
        const val WEEK : Long = DAY * 7
        const val MONTH : Long = 30 * DAY
        const val YEAR :Long = DAY * 365
    }

    private val calendar = Calendar.getInstance().apply { timeInMillis = ms }

    val time: String
    get() = SimpleDateFormat("H:mm",Locale.getDefault()).format(ms)


    val timeNearly: String
        get() {
            val diff = abs(System.currentTimeMillis() - ms)
            return when {
                diff >= YEAR ->
                    "${abs(diff / year)}${context.getString(R.string.short_year)}"
                diff >= MONTH ->
                    "${abs(diff / MONTH)}${context.getString(R.string.short_month)}"
                diff >= WEEK ->
                    "${diff / WEEK}${context.getString(R.string.short_week)}"
                diff >= DAY ->
                    "${diff / DAY}${context.getString(R.string.short_day)}"
                diff >= HOUR ->
                    "${diff / HOUR}${context.getString(R.string.short_hour)}"
                else -> SimpleDateFormat("H:mm", Locale.getDefault()).format(Date(ms))
            }
        }

    val year : Int
        get() =calendar.get(Calendar.YEAR)

    val day : Int
        get() = calendar.get(Calendar.DAY_OF_YEAR)

    val dayAndMonthLocalized : String
        get() {

            val sameYear = year == System.currentTimeMillis().dateTime(context).year

            val pattern = when(Locale.getDefault()){
                RUSSIAN -> if (sameYear) "d MMMM" else "d MMMM YYYY"
                else -> if (sameYear) "MMMM d" else "MMMM d YYYY"
            }
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            return sdf.format(Date(ms))
        }

    val dateNoYear: String
        get() =  SimpleDateFormat("dd.MM", Locale.getDefault()).format(ms)

    val lastOnline : String
        get() = run {

            val now = System.currentTimeMillis().dateTime(context)
            when {
                //today
                day == now.day ->
                    context.getString(R.string.was_at, time)
                //yesterday
                day == now.day-1 || (day - now.day >=360 && System.currentTimeMillis() - ms <= DAY*2) ->
                    context.getString(R.string.was_yesterday_at, time)
                else ->
                    context.getString(R.string.was_ago,timeNearly)
            }
        }
}

@Composable
fun Long.dateTime() = dateTime(LocalContext.current)

fun Long.dateTime(context : Context) = TimeVisualizer(context,this)