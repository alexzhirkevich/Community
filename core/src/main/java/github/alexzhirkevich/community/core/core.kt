package github.alexzhirkevich.community.core

import android.util.Log
import github.alexzhirkevich.community.core.repo.base.IndependentRepository
import kotlin.reflect.KMutableProperty

//@Throws(IllegalArgumentException::class)
///**
// * Returns [Lazy] implementation of [IndependentRepository]
// *  @throws IllegalArgumentException if [T] is not an interface or not from core module
// */
//inline fun <reified T : IndependentRepository> coreProviders() : Lazy<T> =
//    lazy { getIndependentProvider(T::class) }
//
//@Suppress("UNCHECKED_CAST")
//@Throws(IllegalArgumentException::class)
//fun <T : IndependentRepository> getIndependentProvider(clazz : KClass<T>) : T {
//
//    if (clazz.java.superclass != null)
//        throw IllegalArgumentException("Not an interface")
//
//    return when(clazz) {
//        UsersRepository::class -> UsersProviderInstance as T
//
//        CurrentUserRepository::class -> UsersProviderInstance as T
//
//        UserProfileRepository::class -> UserProfileProviderInstance as T
//
//       // SettingsProvider::class -> SettingsProviderInstance as T
//
//        PostsRepository::class -> POSTS_REPOSITORY_INSTANCE as T
//
//        MessagesRepository::class -> MESSAGES_REPOSITORY_INSTANCE as T
//
//        FirebaseRepository::class -> FIREBASE_REPOSITORY_INSTANCE as T
//
//        EventsRepository::class -> EVENTS_REPOSITORY_INSTANCE as T
//
//        ChatsRepository::class -> CHATS_REPOSITORY_INSTANCE as T
//
//        ChannelsRepository::class -> CHANNELS_REPOSITORY_INSTANCE as T
//
//        AuthRepository::class -> AUTH_REPOSITORY_INSTANCE as T
//
//        //AppInitProvider::class -> AppInitProviderInstance as T
//
//        else -> throw IllegalArgumentException("Interface not from core")
//    }
//}


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

//private val UsersProviderInstance : UsersRepository by lazy {  DaggerUsersProviderComponent.create().getProvider() }
//private val UserProfileProviderInstance : UserProfileRepository by lazy { DaggerUserProfileProviderComponent.create().getProvider() }
//private val SettingsProviderInstance : SettingsProvider by lazy { DaggerSettingsProviderComponent.create().getProvider() }
//private val POSTS_REPOSITORY_INSTANCE : PostsRepository by lazy { DaggerPostsProviderComponent.create().getProvider() }
//private val MESSAGES_REPOSITORY_INSTANCE : MessagesRepository by lazy { DaggerMessagesProviderComponent.create().getProvider() }
//private val FIREBASE_REPOSITORY_INSTANCE : FirebaseRepository by lazy { DaggerFirebaseProviderComponent.create().getProvider() }
//private val EVENTS_REPOSITORY_INSTANCE : EventsRepository by lazy { DaggerEventsProviderComponent.create().getProvider() }
//private val CHATS_REPOSITORY_INSTANCE : ChatsRepository by lazy { DaggerChatsProviderComponent.create().getProvider() }
//private val CHANNELS_REPOSITORY_INSTANCE : ChannelsRepository by lazy { DaggerChannelsProviderComponent.create().getProvider() }
//private val AUTH_REPOSITORY_INSTANCE : AuthRepository by lazy { DaggerAuthProviderComponent.create().getProvider() }
//private val AppInitProviderInstance : AppInitProvider by lazy { DaggerAppInitProviderComponent.create().getProvider() }

