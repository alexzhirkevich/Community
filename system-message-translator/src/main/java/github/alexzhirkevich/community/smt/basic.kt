package github.alexzhirkevich.community.smt

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import kotlinx.coroutines.flow.Flow


//@ExperimentalStdlibApi
//@Composable
//fun SystemMessage.translate() : State<TranslatedMessage> =
//    translate(LocalContext.current)
//        .collectAsState(initial = EmptyTranslatedMessage)

@ExperimentalStdlibApi
fun SystemMessage.translate(context: Context) : Flow<TranslatedMessage> =
    SystemMessageTranslator.translate(context,this)

val EmptyTranslatedMessage : TranslatedMessage by lazy {
    TranslatedMessage("")
}

data class TranslatedMessage(
    val text : String,
    val content : List<MediaContent>? = null
)




