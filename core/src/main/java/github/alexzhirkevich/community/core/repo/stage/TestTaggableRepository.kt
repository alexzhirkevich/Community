package github.alexzhirkevich.community.core.repo.stage

import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.SnapshotNotFoundException
import github.alexzhirkevich.community.core.entities.imp.ChannelImpl
import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.Taggable
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TestTaggableRepository @Inject constructor() : TaggableRepository {

    override fun get(tag: String): Flow<Response<Taggable>> {
        return flowOf(
            when (tag) {
                "user" -> Response.Success(
                        UserImpl(
                            id = tag,
                            tag = tag,
                            name = "name",
                            description = "Description"
                        ),false,false
                    )
                "channel" -> Response.Success(
                        ChannelImpl(
                            id = tag,
                            tag = tag,
                            name = "name",
                            description = "Description"
                        ),false,false
                )
                else -> Response.Error(SnapshotNotFoundException())
            }
        )
    }
}