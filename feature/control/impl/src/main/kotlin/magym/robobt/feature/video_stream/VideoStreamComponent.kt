package magym.robobt.feature.video_stream

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import magym.robobt.video_stream.VideoStreamRepository
import org.koin.compose.koinInject

@Composable
internal fun VideoStreamComponent() {
    if (LocalInspectionMode.current) return

    val videoStreamRepository: VideoStreamRepository = koinInject()
    var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        videoStreamRepository.connect()
            .onEach { bitmap -> currentFrame = bitmap.asImageBitmap() }
            .flowOn(Dispatchers.IO)
            .launchIn(this)
    }

    currentFrame?.let { frame ->
        Image(
            bitmap = frame,
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
    }
}