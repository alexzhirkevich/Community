package github.alexzhirkevich.community.features.mediagrid

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader


@Composable
fun MediaContentFlexBox(
    mediaContent: List<MediaContent>,
    storageRepository: StorageDownloader,
    modifier: Modifier = Modifier,
    onContentCLick: (MediaContent) -> Unit
){

    Box() {
        when(mediaContent.size){
            1 -> Content1(
                modifier = modifier,
                content = mediaContent[0],
                storageDownloader = storageRepository) {
                onContentCLick(mediaContent[0])
            }
            2 -> Content2(
                modifier = modifier,
                content = mediaContent,
                storageDownloader = storageRepository,
                onClick = onContentCLick
            )
            3 -> Content3(
                modifier = modifier,
                content = mediaContent,
                storageDownloader = storageRepository,
                onClick = onContentCLick)

            4 -> Content4(
                modifier = modifier,
                content = mediaContent,
                storageDownloader = storageRepository,
                onClick = onContentCLick)
        }
    }
}








