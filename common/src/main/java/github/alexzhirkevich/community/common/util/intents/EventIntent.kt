package github.alexzhirkevich.community.common.util.intents

import android.content.Intent
import android.provider.CalendarContract

class EventIntent private constructor(): Intent(Intent.ACTION_EDIT){
    class Builder : IntentBuilder<EventIntent> {

        var title : String = ""
        var location : String = ""
        var description : String = ""
        var beginTime : Long = System.currentTimeMillis()
        var endTime : Long = System.currentTimeMillis()
        var isAllDay : Boolean = false

        override fun build(): EventIntent =
            EventIntent().apply {
                type = "vnd.android.cursor.item/event"
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                putExtra(CalendarContract.Events.ALL_DAY, isAllDay)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
            }


        fun setTitle(title : String) : Builder{
            this.title = title
            return this
        }

        fun setLocation(location : String) : Builder{
            this.location = location
            return this
        }

        fun setDescription(description : String) : Builder{
            this.description = description
            return this
        }

        fun setBeginTime(time : Long) : Builder{
            this.beginTime = time
            return this
        }

        fun setEndTime(time : Long) : Builder{
            this.endTime = time
            return this
        }

        fun setAllDay(isAllDay : Boolean) : Builder{
            this.isAllDay = isAllDay
            return this
        }
    }
}