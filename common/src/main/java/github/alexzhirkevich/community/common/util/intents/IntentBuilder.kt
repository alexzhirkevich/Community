package github.alexzhirkevich.community.common.util.intents

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

interface IntentBuilder<T : Intent> {

    fun build() : T

    fun start(context : Context) = context.startActivity(build())

    fun start(fragment : Fragment) = fragment.startActivity(build())
}