package github.alexzhirkevich.community.features.mediagrid

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader

@Composable
internal fun Content1(
    content: MediaContent,
    storageDownloader: StorageDownloader,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    MediaContentWidget(
        mediaContent = content,
        storageDownloader = storageDownloader,
        onClick = onClick,
        modifier = modifier
    )
}