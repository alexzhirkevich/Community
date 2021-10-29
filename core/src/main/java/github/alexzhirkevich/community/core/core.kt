package github.alexzhirkevich.community.core

import android.util.Log
import github.alexzhirkevich.community.core.providers.*
import github.alexzhirkevich.community.core.providers.DaggerFirebaseProviderComponent
import github.alexzhirkevich.community.core.providers.base.IndependentProvider
import github.alexzhirkevich.community.core.providers.interfaces.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

@Throws(IllegalArgumentException::class)
/**
 * Returns [Lazy] implementation of [IndependentProvider]
 *  @throws IllegalArgumentException if [T] is not an interface or not from core module
 */
inline fun <reified T : IndependentProvider> coreProviders() : Lazy<T> =
    lazy { getIndependentProvider(T::class) }

@Suppress("UNCHECKED_CAST")
@Throws(IllegalArgumentException::class)
fun <T : IndependentProvider> getIndependentProvider(clazz : KClass<T>) : T {

    if (clazz.java.superclass != null)
        throw IllegalArgumentException("Not an interface")

    return when(clazz) {
        UsersProvider::class -> UsersProviderInstance as T

        UserProfileProvider::class -> UserProfileProviderInstance as T

       // SettingsProvider::class -> SettingsProviderInstance as T

        PostsProvider::class -> PostsProviderInstance as T

        MessagesProvider::class -> MessagesProviderInstance as T

        FirebaseProvider::class -> FirebaseProviderInstance as T

        EventsProvider::class -> EventsProviderInstance as T

        ChatsProvider::class -> ChatsProviderInstance as T

        ChannelsProvider::class -> ChannelsProviderInstance as T

        AuthProvider::class -> AuthProviderInstance as T

        //AppInitProvider::class -> AppInitProviderInstance as T

        else -> throw IllegalArgumentException("Interface not from core")
    }
}


/**
 * Returns [T] instance filled by map fields.
 * @throws IllegalArgumentException if class don't have a non-argument constructor. Use [fillObject] instead
 * */
@kotlin.jvm.Throws(IllegalArgumentException::class)
inline fun <reified T : Any> Map<String,*>.toObject(): T {

    val instance = try {
            T::class.java.newInstance()
        } catch (t: Throwable) {
            throw IllegalArgumentException(
                "${T::class.simpleName} don't have a non-argument constructor and emptyInstance param is null "
            )
        }
    fillObject(instance)
    return instance
}

/**
 * Fills [T] instance by map fields.
 * @throws IllegalArgumentException if class don't have a non-argument constructor. Use [fillObject] instead
 * */
inline fun <reified T : Any> Map<String,*>.fillObject(instance : T) {
    T::class.members.filterIsInstance<KMutableProperty<*>>()
        .filter { it.name in keys }
        .forEach {
            val propValue = get(it.name)
            try {
                it.setter.call(instance, propValue)
            }catch (t : Throwable){
                Log.w("Core","Failed to fill property ${it.name} with $propValue")
            }}
}

private val UsersProviderInstance : UsersProvider by lazy {  DaggerUsersProviderComponent.create().getProvider() }
private val UserProfileProviderInstance : UserProfileProvider by lazy { DaggerUserProfileProviderComponent.create().getProvider() }
//private val SettingsProviderInstance : SettingsProvider by lazy { DaggerSettingsProviderComponent.create().getProvider() }
private val PostsProviderInstance : PostsProvider by lazy { DaggerPostsProviderComponent.create().getProvider() }
private val MessagesProviderInstance : MessagesProvider by lazy { DaggerMessagesProviderComponent.create().getProvider() }
private val FirebaseProviderInstance : FirebaseProvider by lazy { DaggerFirebaseProviderComponent.create().getProvider() }
private val EventsProviderInstance : EventsProvider by lazy { DaggerEventsProviderComponent.create().getProvider() }
private val ChatsProviderInstance : ChatsProvider by lazy { DaggerChatsProviderComponent.create().getProvider() }
private val ChannelsProviderInstance : ChannelsProvider by lazy { DaggerChannelsProviderComponent.create().getProvider() }
private val AuthProviderInstance : AuthProvider by lazy { DaggerAuthProviderComponent.create().getProvider() }
//private val AppInitProviderInstance : AppInitProvider by lazy { DaggerAppInitProviderComponent.create().getProvider() }

