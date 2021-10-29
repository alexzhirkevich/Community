package github.alexzhirkevich.community.data.viewmodels

import github.alexzhirkevich.community.core.entities.imp.GroupImpl
import github.alexzhirkevich.community.data.ChatWrap
import github.alexzhirkevich.community.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalStdlibApi
class ChatsViewModel : DataViewModel<List<ChatWrap>>() {

    override val sourceFlow: Flow<DataState<List<ChatWrap>>> = flow {

        val list = List(30) {
            ChatWrap.GroupWrap(
                group = GroupImpl(
                    id = it.toString(),
                    imageUri = "https://firebasestorage.googleapis.com/v0/b/messenger-302121.appspot.com/o/userdata%2FOUuBLqSJjvf2k9UtOouGJx1cRwf2%2Fimages%2F1627738587225.?alt=media&token=519de77c-6bad-45cd-b8f7-34f9bb6bc2f9",
                    name = "Group"
                ),
                unreadCount = 3,
                membersCount = 15
            )
        }
        emit(DataState.Success(list))
    }

    init {
        update()
    }
}