package github.alexzhirkevich.community.core.repo.imp

import android.util.Log
import github.alexzhirkevich.community.core.BuildConfig
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.SnapshotNotFoundException
import github.alexzhirkevich.community.core.entities.interfaces.Taggable
import github.alexzhirkevich.community.core.repo.interfaces.ChannelsRepository
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class TaggableRepositoryImpl @Inject constructor(
    private val usersRepository: UsersRepository,
    private val channelsRepository: ChannelsRepository
) : TaggableRepository {

    override fun get(tag: String): Flow<Response<Taggable>> =
        usersRepository.findByTag(tag).combine(channelsRepository.findByTag(tag)) { user, channel ->
            when {
                user is Response.Success ->
                    user
                channel is Response.Success ->
                    channel
                else -> Response.Error(SnapshotNotFoundException())
            }
        }
            .catch {
                if (BuildConfig.DEBUG) {
                    Log.e(
                        javaClass.simpleName,
                        "Failed to get by tag $tag", it
                    )
                }
            }
}