package github.alexzhirkevich.community.common.util.intents

import android.content.Intent
import android.provider.AlarmClock

class AlarmIntent private constructor(): Intent(AlarmClock.ACTION_SET_ALARM) {

    class Builder : IntentBuilder<AlarmIntent> {

        var message : String = ""
        var days : ArrayList<Int> = arrayListOf()
        var hours : Int = 0
        var minutes : Int = 0

        override fun build(): AlarmIntent =
            AlarmIntent().apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, message)
                putExtra(AlarmClock.EXTRA_DAYS, days)
                putExtra(AlarmClock.EXTRA_HOUR, hours)
                putExtra(AlarmClock.EXTRA_MINUTES, minutes)

        }

        fun setMessage(message : String) : Builder{
            this.message = message
            return this
        }

        fun setDays(days : ArrayList<Int>) : Builder{
            this.days = days
            return this
        }

        fun setHours(hours : Int) : Builder {
            this.hours = hours
            return this
        }

        fun setMinutes(minutes : Int) : Builder {
            this.minutes = minutes
            return this
        }
    }
}