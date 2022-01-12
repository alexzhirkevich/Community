package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.repo.base.IndependentRepository
import com.google.firebase.firestore.CollectionReference

interface FirebaseRepository : IndependentRepository {

    val channelsCollection: CollectionReference

    val usersCollection: CollectionReference

    val chatsCollection: CollectionReference

    val eventCollection: CollectionReference

    companion object {

        const val COLLECTION_MEDIA_MESSAGES = "media_messages"
        const val COLLECTION_VOICE_MESSAGES = "voice_messages"
        const val COLLECTION_CONTACTS = "contacts"
        const val COLLECTION_ADMINS = "admins"
        const val COLLECTION_USERS = "users"
        const val COLLECTION_CHATS = "chats"
        const val COLLECTION_EVENTS = "events"
        const val COLLECTION_CHANNELS = "channels"
        const val COLLECTION_POSTS = "posts"
        const val COLLECTION_MESSAGES = "messages"

        const val PRIVATE = "private"
        const val SETTINGS = "settings"
        const val PUBLIC = "public"

        const val FIELD_USERNAME = "username"
        const val FIELD_USERNAME_SEARCH = "searchUsername"
        const val FIELD_PHONE = "phone"
        const val FIELD_SUBCRIBERS = "subscribers"
        const val FIELD_IMAGE_URI = "imageUri"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_TAG = "tag"
        const val FIELD_CHAT_ID = "chatId"
        const val FIELD_SEARCH_TAG = "searchTag"
        const val FIELD_CREATOR_ID = "creatorId"
        const val FIELD_TIME= "time"
        const val FIELD_POST_VIEWS= "views"
        const val FIELD_POST_REPOSTS= "reposts"
        const val FIELD_ID = "id"
        const val FIELD_TYPE = "type"
        const val FIELD_NAME = "name"
        const val FIELD_SEARCH_NAME = "searchName"
        const val FIELD_LAST_MESSAGE = "lastMessage"
        const val FIELD_REPLY_TO = "replyTo"
        const val FIELD_LAST_POST_TIME = "lastPostTime"
        const val FIELD_LAST_VIEWED_POST = "lastViewedPost"
        const val FIELD_LAST_VISIT_TIME = "lastPostTime"
        const val FIELD_ONLINE = "online"
        const val FIELD_LAST_ONLINE = "lastOnline"
        const val FIELD_VOICE_URI = "voiceUri"
        const val FIELD_VOICE_LEN = "voiceLen"
        const val FIELD_MEDIA_CONTENT = "mediaContent"
        const val FIELD_REFERENCE = "reference"
        const val FIELD_NOTIFY_TOKEN = "notificationToken"
        const val PRIVATE_USERS_INFO = "privateUsersInfo"

        const val USER_DATA = "userdata"
        const val IMAGES = "images"
        const val VIDEOS = "videos"
        const val GIFS = "videos"
        const val VOICES = "voices"
        const val DOCUMENTS = "voices"

        const val URL_BASE = "https://firemessenger.com/"
        const val LINK_CHANNEL = "joinchannel/"
        const val LINK_CHAT = "joinchat/"
    }
}
