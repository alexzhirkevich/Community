package github.alexzhirkevich.community.smt

import android.content.Context
import github.alexzhirkevich.community.core.coreProviders
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import github.alexzhirkevich.community.smt.translators.ChannelSystemMessageTranslator
import github.alexzhirkevich.community.smt.translators.ChatSystemMessageTranslator
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
internal object SystemMessageTranslator : SMTBase() {

    private val usersProvider : UsersProvider by coreProviders()

    private val sources: List<SMTBase> = listOf(
        ChannelSystemMessageTranslator(usersProvider),
        ChatSystemMessageTranslator(usersProvider)
    )

    override fun findMethod(code: Int): ((Context, SystemMessage) -> Flow<TranslatedMessage>)? =
        sources.firstNotNullOfOrNull { it.findMethod(code) }

}