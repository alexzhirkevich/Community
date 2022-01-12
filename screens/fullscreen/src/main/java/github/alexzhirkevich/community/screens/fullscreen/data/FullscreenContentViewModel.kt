package github.alexzhirkevich.community.screens.fullscreen.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import kotlinx.coroutines.GlobalScope


class FullscreenContentViewModel constructor (
    val content: List<MediaContent>,
    private val globalStorageRepository: StorageDownloader,
) : ViewModel(), StorageDownloader by globalStorageRepository {

    class Factory(
        private val content: List<MediaContent>,
        private val globalStorageRepository: StorageRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FullscreenContentViewModel(content,globalStorageRepository) as T
        }
    }
}