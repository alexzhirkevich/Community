package github.alexzhirkevich.community.smt

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.repo.stage.TestUsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow


@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@Composable
fun SystemMessage.translate() : State<TranslatedMessage> =
    translate(LocalContext.current)
        .collectAsState(initial = EmptyTranslatedMessage)

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
fun SystemMessage.translate(context: Context) : Flow<TranslatedMessage> =
    systemMessageTranslator.translate(context,this)

val EmptyTranslatedMessage : TranslatedMessage by lazy {
    TranslatedMessage("")
}

data class TranslatedMessage(
    val text : String,
    val content : List<MediaContent>? = null
)

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
private val systemMessageTranslator : SystemMessageTranslator by lazy {
//    val repo = DaggerSystemMessageComponent.create()
//        .usersRepository
    val repo = TestUsersRepository()
    SystemMessageTranslator(repo)

}




