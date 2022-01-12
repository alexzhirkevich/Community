package github.alexzhirkevich.community.smt

import android.content.Context
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import github.alexzhirkevich.community.smt.translators.ChannelSystemMessageTranslator
import github.alexzhirkevich.community.smt.translators.ChatSystemMessageTranslator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalStdlibApi
class SystemMessageTranslator @Inject constructor(
    usersProvider : UsersRepository
) : SMTBase() {

    private val sources: List<SMTBase> = listOf(
        ChannelSystemMessageTranslator(usersProvider),
        ChatSystemMessageTranslator(usersProvider)
    )

    override fun findMethod(code: Int): ((Context, SystemMessage) -> Flow<TranslatedMessage>)? =
        sources.firstNotNullOfOrNull { it.findMethod(code) }

}