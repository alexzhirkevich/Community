package github.alexzhirkevich.community.smt.translators

import android.content.Context
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import github.alexzhirkevich.community.smt.R
import github.alexzhirkevich.community.smt.SMTBase
import github.alexzhirkevich.community.smt.TranslatedMessage
import github.alexzhirkevich.community.smt.TranslationSource
import kotlinx.coroutines.flow.*

@ExperimentalStdlibApi
internal class ChannelSystemMessageTranslator(
    private val usersProvider: UsersProvider) : SMTBase() {

    @TranslationSource(SystemMessage.MESSAGE_CHANNEL_CREATED)
    fun channelCreated(context: Context, msg: SystemMessage): Flow<TranslatedMessage> =
        flowOf(TranslatedMessage(context.getString(R.string.sm_channel_created)))


    @TranslationSource(SystemMessage.MESSAGE_CHANNEL_AVATAR_CHANGED)
    fun channelAvatarChanged(context: Context, msg: SystemMessage): Flow<TranslatedMessage> {

        val imageUrl = msg.metainf?.get(SystemMessage.META_URL)

        val content = if (imageUrl != null)
            listOf(MediaContent(MediaContent.IMAGE, imageUrl))
        else null

        return flowOf(TranslatedMessage(
            text = context.getString(R.string.sm_channel_avatar_changed),
            content = content
        ))
    }

    @TranslationSource(SystemMessage.MESSAGE_CHANNEL_NAME_CHANGED)
    fun channelNameChanged(context: Context, msg: SystemMessage): Flow<TranslatedMessage> {

        val raw = context.getString(R.string.sm_channel_name_changed)

        val to = msg.metainf?.get(SystemMessage.META_TO)?.let {
            getUser(usersProvider, it)
        } ?: return emptyFlow()
        
        return to.map {
            TranslatedMessage(
                raw.format(it.name)
            )
        }
    }
}