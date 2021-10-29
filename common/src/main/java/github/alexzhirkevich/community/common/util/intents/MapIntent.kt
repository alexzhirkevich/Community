package github.alexzhirkevich.community.common.util.intents

import android.content.Intent
import android.net.Uri
import github.alexzhirkevich.community.common.util.intents.IntentBuilder

class MapIntent private  constructor(uri : Uri) : Intent(Intent.ACTION_VIEW,uri) {

    class Builder : IntentBuilder<MapIntent> {

        var latitude : Float = 0f
        var longitude : Float = 0f
        var query : String = ""

        override fun build(): MapIntent {
            val uri ="geo:${latitude},${longitude}?q=${query}"
            return MapIntent(Uri.parse(uri))
        }

        fun setLatitude(latitude : Float) : Builder {
            this.latitude = latitude
            return this
        }

        fun setLongitude(longitude : Float) : Builder {
            this.longitude = longitude
            return this
        }

        fun setQuery(query : String) : Builder {
            this.query = query
            return this
        }
    }
}