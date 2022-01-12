package github.alexzhirkevich.community.smt.translators

import android.content.Context
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import github.alexzhirkevich.community.smt.R
import github.alexzhirkevich.community.smt.SMTBase
import github.alexzhirkevich.community.smt.TranslatedMessage
import github.alexzhirkevich.community.smt.TranslationSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

@ExperimentalStdlibApi
internal class ChatSystemMessageTranslator(
    private val usersProvider: UsersRepository
) : SMTBase() {

    @TranslationSource(SystemMessage.MESSAGE_CHAT_INVITED)
    fun chatInvited(context: Context, message: SystemMessage) : Flow<TranslatedMessage> {

        val by = message.metainf?.get(SystemMessage.META_BY)?.let {
            getUser(usersProvider, it)
        }

        val who = message.metainf?.get(SystemMessage.META_WHO)?.let {
            getUser(usersProvider, it)
        }
        if (by == null || who == null)
            return emptyFlow()

        val raw = context.getString(R.string.sm_chat_invited)

        return by.combine(who) { b, w ->
                TranslatedMessage(
                    raw.format(R.string.sm_chat_invited, b, w)
                )
        }
    }

    @TranslationSource(SystemMessage.MESSAGE_CHAT_AVATAR_CHANGED)
    fun chatAvatarChanged(context: Context, message: SystemMessage) : Flow<TranslatedMessage> {

        val by = message.metainf?.get(SystemMessage.META_BY)?.let {
            getUser(usersProvider, it)
        }
        val content = message.metainf?.get(SystemMessage.META_URL)?.let {
            listOf(MediaContent(MediaContent.IMAGE,it))
        }

        if (by == null)
            return emptyFlow()

        val raw = context.getString(R.string.sm_chat_avatar_changed)

        return by.map {
            TranslatedMessage(
                text = raw.format(it.name),
                content = content
            )
        }
    }

    @TranslationSource(SystemMessage.MESSAGE_CHAT_NAME_CHANGED)
    fun chatNameChanged(context: Context,message: SystemMessage) : Flow<TranslatedMessage>{
        val to = message.metainf?.get(SystemMessage.META_TO)

        val by = message.metainf?.get(SystemMessage.META_BY)?.let {
            getUser(usersProvider,it)
        }?: return emptyFlow()

        val raw = context.getString(R.string.sm_chat_name_changed)

        return by.map {
            TranslatedMessage(
                raw.format(it.name,to.orEmpty())
            )
        }
    }
}