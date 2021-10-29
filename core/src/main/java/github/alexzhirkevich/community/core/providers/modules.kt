package github.alexzhirkevich.community.core.providers

import github.alexzhirkevich.community.core.providers.base.Provider
import github.alexzhirkevich.community.core.providers.imp.*
import github.alexzhirkevich.community.core.providers.interfaces.*
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


interface ProviderModule<IProvider : Provider, ProviderImp : IProvider> {

    fun provide(provider : ProviderImp) : IProvider
}


@Module
abstract class CurrentUserProviderModule
    : ProviderModule<CurrentUserProvider,CurrentUserProviderImp>{

    @Binds
    abstract override fun provide(provider: CurrentUserProviderImp): CurrentUserProvider
}

@Module
abstract class UserProfileProviderModule :
    ProviderModule<UserProfileProvider, UserProfileProviderImp> {

    @Binds
    abstract override fun provide(provider: UserProfileProviderImp): UserProfileProvider
}


@ExperimentalCoroutinesApi
@Module
abstract class UsersProviderModule : ProviderModule<UsersProvider, UsersProviderImp> {

    @Binds
    abstract override fun provide(provider: UsersProviderImp): UsersProvider
}

@ExperimentalCoroutinesApi
@FlowPreview
@Module
abstract class ChatsProviderModule : ProviderModule<ChatsProvider, ChatsProviderImp> {

    @Binds
    abstract override fun provide(provider: ChatsProviderImp): ChatsProvider
}

@ExperimentalCoroutinesApi
@Module
abstract class ChannelsProviderModule : ProviderModule<ChannelsProvider, ChannelsProviderImp> {

    @Binds
    abstract override fun provide(provider: ChannelsProviderImp): ChannelsProvider
}

@Module
abstract class AuthProviderModule : ProviderModule<AuthProvider, AuthProviderImp> {

    @Binds
    abstract override fun provide(provider: AuthProviderImp): AuthProvider
}

@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Module
abstract class ContactsProviderModule : ProviderModule<ContactsProvider, ContactsProviderImp> {

    @Binds
    abstract override fun provide(provider: ContactsProviderImp): ContactsProvider
}

@ExperimentalCoroutinesApi
@Module
abstract class PostsProviderModule : ProviderModule<PostsProvider, PostsProviderImp> {

    @Binds
    abstract override fun provide(provider: PostsProviderImp): PostsProvider
}


@ExperimentalCoroutinesApi
@Module
abstract class SettingsProviderModule : ProviderModule<SettingsProvider, SettingsProviderImp> {
    @Binds
    abstract override fun provide(provider: SettingsProviderImp): SettingsProvider
}

@Module
abstract class FirebaseProviderModule : ProviderModule<FirebaseProvider, FirebaseProviderImp> {

    @Binds
    abstract override fun provide(provider: FirebaseProviderImp): FirebaseProvider
}

@ExperimentalCoroutinesApi
@Module
abstract class MessagesProviderModule : ProviderModule<MessagesProvider, MessagesProviderImp> {

    @Binds
    abstract override fun provide(provider: MessagesProviderImp): MessagesProvider
}

@ExperimentalCoroutinesApi
@Module
abstract class EventsProviderModule : ProviderModule<EventsProvider, EventsProviderImp> {

    @Binds
    abstract override fun provide(provider: EventsProviderImp): EventsProvider
}

@Module
abstract class AppInitProviderModule : ProviderModule<AppInitProvider, AppInitProviderImp> {

    @Binds
    abstract override fun provide(provider: AppInitProviderImp): AppInitProvider
}