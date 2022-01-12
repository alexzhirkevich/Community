package github.alexzhirkevich.community.core.di

import android.content.ContentResolver
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.community.core.entities.MessageBuilder
import github.alexzhirkevich.community.core.entities.MessageBuilderImpl
import github.alexzhirkevich.community.core.entities.PostBuilderImpl
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.core.entities.interfaces.Post
import github.alexzhirkevich.community.core.logger.FileLogger
import github.alexzhirkevich.community.core.logger.Logger
import github.alexzhirkevich.community.core.repo.imp.*
import github.alexzhirkevich.community.core.repo.imp.storage.StorageRepositoryImp
import github.alexzhirkevich.community.core.repo.interfaces.*
import github.alexzhirkevich.community.core.repo.stage.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Qualifier

@Qualifier
annotation class Stage

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@Module(
    includes = [
        CoreBindModule::class
    ]
)
class CoreModule {

    @Provides
    fun provideContentResolver(context : Context) : ContentResolver =
        context.contentResolver
}

@Suppress("FunctionName")
@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Module
interface CoreBindModule {

    @Binds
    fun bindAuthRepository(authRepositoryImp: AuthRepositoryImp) : AuthRepository

    @Binds
    fun bindChannelProfileRepository(channelProfileRepositoryImp: ChannelProfileRepositoryImp)
            : ChannelProfileRepository

    @Binds
    fun bindChannelRepository(channelsRepositoryImp: ChannelsRepositoryImp) : ChannelsRepository

    @Binds
    @Stage
    fun bindChannelRepository_Stage(channelsRepositoryImp: TestChannelsRepository) : ChannelsRepository

    @Binds
    fun bindChatsRepository(chatsRepositoryImp: ChatsRepositoryImp) : ChatsRepository

    @Binds
    @Stage
    fun bindChatsRepository_Stage(chatsRepositoryImp: TestChatsRepository) : ChatsRepository

    @Binds
    fun bindContactsRepository(contactsRepositoryImp: ContactsRepositoryImp) : ContactsRepository

    @Binds
    fun bindCurrentUserRepository(currentUserRepositoryImp: CurrentUserRepositoryImp) : CurrentUserRepository

    @Binds
    @Stage
    fun bindCurrentUserRepository_Stage(currentUserRepositoryImp: TestCurrentUserRepository) : CurrentUserRepository

    @Binds
    fun bindEventsRepository(eventsRepositoryImp: EventsRepositoryImp) : EventsRepository

    @Binds
    fun bindFirebaseRepository(firebaseRepositoryImp: FirebaseRepositoryImp) : FirebaseRepository

   // @Binds
  // fun bindMessagesRepository(messagesRepositoryImp: MessagesRepositoryImp) : MessagesRepository

  //  @Binds
  //  @Stage
  //  fun bindMessagesRepository_Stage(messagesRepositoryImp: TestMessagesRepository) : MessagesRepository

    @Binds
    fun bindPhoneAuthRepository(phoneAuthRepositoryImp: PhoneAuthRepositoryImp) : PhoneAuthRepository

   // @Binds
    //fun bindPostsRepository(postsRepositoryImp: PostsRepositoryImp) : PostsRepository

  //  @Binds
   // @Stage
   // fun bindPostsRepository_Stage(postsRepositoryImp: TestPostsRepository) : PostsRepository

    @Binds
    fun bindSettingsRepository(settingsRepositoryImp: SettingsRepositoryImp) : SettingsRepository

    @Binds
    fun bindStorageRepository(storageRepositoryImp: StorageRepositoryImp) : StorageRepository

    @Binds
    @Stage
    fun bindStorageRepository_Stage(storageRepositoryImp: TestStorageRepository) : StorageRepository

    @Binds
    fun bindUserProfileRepository(userProfileRepositoryImp: UserProfileRepositoryImp) : UserProfileRepository

    @Binds
    @Stage
    fun bindUserProfileRepository_Stage(userProfileRepositoryImp: TestUserProfileRepository) : UserProfileRepository

    @Binds
    fun bindUsersRepository(usersRepositoryImp: UsersRepositoryImp) : UsersRepository

    @Binds
    @Stage
    fun bindUsersRepository_Stage(usersRepositoryImp: TestUsersRepository) : UsersRepository

    @Binds
    fun bindTaggableRepository(taggableRepositoryImpl: TaggableRepositoryImpl) : TaggableRepository

    @Binds
    @Stage
    fun bindTaggableRepository_Stage(taggableRepositoryImpl: TestTaggableRepository) : TaggableRepository

    @Binds
    fun bindMessageBuilder_Message(messageBuilder: MessageBuilderImpl) : MessageBuilder<Message>

    @Binds
    fun bindMessageBuilder_Post(messageBuilder: PostBuilderImpl) : MessageBuilder<Post>

    @Binds
    fun bindLogger(logger: FileLogger) : Logger
}

