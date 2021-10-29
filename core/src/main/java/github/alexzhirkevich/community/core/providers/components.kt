package github.alexzhirkevich.community.core.providers

import android.content.ContentResolver
import android.content.Context
import github.alexzhirkevich.community.core.providers.base.Provider
import github.alexzhirkevich.community.core.providers.interfaces.*
import dagger.BindsInstance
import dagger.Component
import github.alexzhirkevich.community.core.providers.imp.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

interface ProviderComponent<P: Provider> {
    fun getProvider() : P
}

@Singleton
@Component(modules = [
    FirebaseProviderModule::class,
    UsersProviderModule::class
])
interface UsersProviderComponent : ProviderComponent<UsersProvider>



@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    UserProfileProviderModule::class
])
interface UserProfileProviderComponent : ProviderComponent<UserProfileProvider>


@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    CurrentUserProviderModule::class
])
interface StorageProviderComponent : ProviderComponent<StorageProviderImp>


@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    SettingsProviderModule::class
])
interface SettingsProviderComponent : ProviderComponent<SettingsProvider>{
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setScope(scope : CoroutineScope) : Builder

        fun build() : SettingsProviderComponent
    }
}


@Singleton
@Component(modules = [
    FirebaseProviderModule::class,
    CurrentUserProviderModule::class,
    PostsProviderModule::class
])
interface PostsProviderComponent : ProviderComponent<PostsProvider>


@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class
])
interface PhoneAuthProviderComponent : ProviderComponent<PhoneAuthProviderImp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setCallback(callback : PhoneAuthCallback?) : Builder

        fun build() : PhoneAuthProviderComponent
    }
}


@Singleton
@Component(modules = [
    FirebaseProviderModule::class,
    MessagesProviderModule::class
])
interface MessagesProviderComponent : ProviderComponent<MessagesProvider>


@Singleton
@Component(modules = [
    FirebaseProviderModule::class
])
interface FirebaseProviderComponent : ProviderComponent<FirebaseProvider>


@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    EventsProviderModule::class
])
interface EventsProviderComponent : ProviderComponent<EventsProvider>


@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Singleton
@Component(modules = [FirebaseProviderModule::class, UsersProviderModule::class])
interface ContactsProviderComponent : ProviderComponent<ContactsProviderImp> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun setContentResolver(contentResolver: ContentResolver) : Builder

        fun build() : ContactsProviderComponent
    }
}


@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    ChatsProviderModule::class
])
interface ChatsProviderComponent : ProviderComponent<ChatsProvider>


@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    ChannelsProviderModule::class
])
interface ChannelsProviderComponent : ProviderComponent<ChannelsProvider>


@Singleton
@Component(modules = [
    FirebaseProviderModule::class
])
interface ChannelProfileProviderComponent : ProviderComponent<ChannelProfileProviderImp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setId(id : String) : Builder

        fun build() : ChannelProfileProviderComponent
    }
}


@Singleton
@Component(modules = [
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    AuthProviderModule::class
])
interface AuthProviderComponent : ProviderComponent<AuthProvider>


@ExperimentalStdlibApi
@Singleton
@Component(modules = [
    AuthProviderModule::class,
    UsersProviderModule::class,
    FirebaseProviderModule::class,
    SettingsProviderModule::class,
    ContactsProviderModule::class,
    AppInitProviderModule::class
])
interface AppInitProviderComponent : ProviderComponent<AppInitProvider>