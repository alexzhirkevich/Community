package github.alexzhirkevich.community.smt

import android.content.Context
import android.util.Log
import github.alexzhirkevich.community.core.BuildConfig
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import github.alexzhirkevich.community.smt.translators.ChannelSystemMessageTranslator
import kotlinx.coroutines.flow.*
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf

@Target(AnnotationTarget.FUNCTION)
annotation class TranslationSource(val code : Int)

internal interface ISystemMessageTranslator {

    fun translate(context : Context, msg : SystemMessage) : Flow<TranslatedMessage>
}


@ExperimentalStdlibApi
internal abstract class SMTBase : ISystemMessageTranslator {

    override fun translate(context: Context, msg: SystemMessage): Flow<TranslatedMessage> {
        return findMethod(msg.message)?.invoke(context, msg)
            ?.catch { err ->
                if (BuildConfig.DEBUG)
                    Log.e(javaClass.simpleName,
                        "Failed to translate system message : $msg",
                        err
                    )
            } ?: emptyFlow()
    }

    private val cached = mutableMapOf<Int, (Context, SystemMessage) -> Flow<TranslatedMessage>>()

    @Suppress("UNCHECKED_CAST")
    open fun findMethod(code: Int): ((Context, SystemMessage) -> Flow<TranslatedMessage>)? {

        return cached[code] ?: this::class.members.find {
            it is KFunction &&
                    it.isAccessible
                    it.annotations.any { it is TranslationSource && it.code == code } &&
                    it.returnType == typeOf<Flow<TranslatedMessage>>() &&
                    it.parameters.size == 3 &&
                    it.parameters[1].type == typeOf<Context>() &&
                    it.parameters[2].type == typeOf<SystemMessage>()
        }?.let {
            val method = { c: Context, s: SystemMessage ->
                it.call(this,c, s) as Flow<TranslatedMessage>
            }
            cached[code] = method

            return@let method

        }
    }

    protected fun getUser(usersProvider: UsersProvider,id : String) : Flow<User> =
        usersProvider.get(id)
            .mapNotNull { (it as Response.Success).value }
            .catch { err ->
                if (BuildConfig.DEBUG)
                    Log.e(
                        SystemMessageTranslator::class.java.simpleName,
                        "Failed to get user", err
                    )
            }
}
