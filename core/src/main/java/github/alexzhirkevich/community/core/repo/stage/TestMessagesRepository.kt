package github.alexzhirkevich.community.core.repo.stage

import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.imp.MessageImpl
import github.alexzhirkevich.community.core.entities.imp.SystemMessageImpl
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.repo.interfaces.MessagesRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.math.roundToLong
import kotlin.random.Random


private val long1 = MediaContent(
    MediaContent.IMAGE,
    url ="https://s1.1zoom.ru/b5050/596/Evening_Forests_Mountains_Firewatch_Campo_Santo_549147_1920x1080.jpg",
    thumbnailUrl = "https://s1.1zoom.ru/b5050/596/Evening_Forests_Mountains_Firewatch_Campo_Santo_549147_1920x1080.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 1920,
    height = 1080
)
private val long2 = MediaContent(
    MediaContent.IMAGE,
    url = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Aspect_ratio_-_4x3.svg/1280px-Aspect_ratio_-_4x3.svg.png",
    thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Aspect_ratio_-_4x3.svg/1280px-Aspect_ratio_-_4x3.svg.png",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 1280,
    height = 960
)
private val long3 = MediaContent(
    MediaContent.IMAGE,
    url = "https://c4.wallpaperflare.com/wallpaper/214/187/691/video-games-video-game-art-ultrawide-ultra-wide-need-for-speed-heat-hd-wallpaper-preview.jpg",
    thumbnailUrl = "https://c4.wallpaperflare.com/wallpaper/214/187/691/video-games-video-game-art-ultrawide-ultra-wide-need-for-speed-heat-hd-wallpaper-preview.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 3440,
    height = 1080
)
private val high1 = MediaContent(
    MediaContent.IMAGE,
    url ="https://s1.1zoom.ru/b3556/124/Texture_Multicolor_526935_1080x1920.jpg",
    thumbnailUrl = "https://s1.1zoom.ru/b3556/124/Texture_Multicolor_526935_1080x1920.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 1090,
    height = 1920
)
private val high2 = MediaContent(
    MediaContent.IMAGE,
    url = "https://www.earthinpictures.com/world/france/paris/eiffel_tower_960x1280.jpg",
    thumbnailUrl = "https://www.earthinpictures.com/world/france/paris/eiffel_tower_960x1280.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 960,
    height = 1280
)

private val content = listOf(
    long2, long3
)


class TestMessagesRepository @Inject constructor() : MessagesRepository {

    private val usersRepository : UsersRepository =
        TestUsersRepository()

    private var msgId = 30

    private var messages : MutableList<Response<Sendable>> = List(30) {

        Response.Success(
            when {
                it == 29 -> SystemMessageImpl(
                    id = it.toString(),
                    chatId = "collection",
                    time = System.currentTimeMillis() - it * 6 * 60 * 60 * 1000,
                    message = SystemMessage.MESSAGE_CHANNEL_CREATED
                )

                it == 15 -> MessageImpl(
                    id = it.toString(),
                    content = content,
                    chatId = "collection",
                    isViewed = true,
                    senderId = usersRepository.currentUserId,
                    time = System.currentTimeMillis() - it * 6 * 60 * 60 * 1000,
                    text = "Media content message "
                )

                Random.nextBoolean() -> MessageImpl(
                    id = it.toString(),
                    chatId = "collection",
                    isViewed = true,
                    senderId = usersRepository.currentUserId,
                    time = System.currentTimeMillis() - it * 6 * 60 * 60 * 1000,
                    text = buildString {
                        repeat(Random.nextInt(1,20)) {
                            append("gdfj fsddsf f ")
                        }
                    }
                )

                else -> MessageImpl(
                    id = it.toString(),
                    chatId = "collection",
                    senderId = "out",
                    isViewed = it != 0,
                    time = System.currentTimeMillis() - it * 6 * 60 * 60 * 1000,
                    text = buildString {
                        repeat(Random.nextInt(1,20)) {
                            append("fshgd dfs")
                        }
                    }
                )
            }, false, false
        )
    }.sortedBy {
        it.value.time
    }.toMutableList()

    val flow = MutableStateFlow<List<Response<Sendable>>>(messages)

    override fun last(chatId: String): Flow<Response<Sendable>> {
        TODO("Not yet implemented")
    }

    override fun get(id: String, collectionID: String): Flow<Response<Sendable>> {
        TODO("Not yet implemented")
    }

    override suspend fun create(entity: Sendable) {
       if (entity is Message) {
           entity.senderId = usersRepository.currentUserId
           entity.id = (++msgId).toString()
           entity.isViewed = false
           messages = ArrayList(messages)
           messages.add(Response.Success(
               entity,
               isFromCache = false,
               hasPendingWrites = false)
           )
           flow.tryEmit(messages)
       }
    }

    override suspend fun delete(entity: Sendable) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: String, collection: String) {
        TODO("Not yet implemented")
    }

    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Sendable>>> {
        return flow
    }
}