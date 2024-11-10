package de.fhkiel.temi.robogguide.ui.theme.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ErrorResult
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import de.fhkiel.temi.robogguide.R
import java.net.URL

@Composable
fun LoadingVideo(
    url: URL,
    modifier: Modifier = Modifier.fillMaxSize(),
    contentScale: ContentScale = ContentScale.FillBounds
) {
    val uri: Uri = Uri.parse(url.toString())
    val videoId = uri.getQueryParameter("v")

    val videoThumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"

    var isLoading by remember { mutableStateOf(false) }
    var noError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (videoId == null) {
        Log.e("LoadingVideo", "Error parsing video URL")
        return
    }
    Box(contentAlignment = Alignment.Center, modifier = modifier) {

        AsyncImage(
            model = videoThumbnailUrl,
            contentDescription = "Exponat Image",
            contentScale = contentScale,
            onLoading = {
                isLoading = true
                Log.i("LoadingVideo", "Loading image")
            },
            onSuccess = {
                isLoading = false
                noError = true
                Log.i("LoadingVideo", "Image loaded")
            },
            onError = { error ->
                isLoading = false
                val errorResult: ErrorResult = error.result
                Log.e(
                    "LoadingVideo",
                    "Error loading image. ${errorResult.throwable.message.toString()}"
                )
            },
            error = painterResource(id = R.drawable.computermuseum_maerz_23),
        )

        if (noError) {
            CustomIconButton(
                iconId = R.drawable.play_arrow_right,
                contentDescription = "Play Video",
                onClick = { showDialog = true },
                modifier = Modifier.size(128.dp)
            )
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }


    if (showDialog) {

        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                factory = { context ->
                        YouTubePlayerView(context).apply {
                            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    super.onReady(youTubePlayer)
                                    youTubePlayer.loadVideo(videoId, 0f)
                                }

                                override fun onStateChange(
                                    youTubePlayer: YouTubePlayer,
                                    state: PlayerConstants.PlayerState
                                ) {
                                    super.onStateChange(youTubePlayer, state)
                                    if (state == PlayerConstants.PlayerState.ENDED) {
                                        showDialog = false
                                    }
                                    Log.d("LoadingVideo", "State changed: $state")
                                }
                            })
                        }
                    }
                )

                CustomIconButton(
                    iconId = R.drawable.baseline_cancel_24,
                    onClick = { showDialog = false },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

        }
    }
}
