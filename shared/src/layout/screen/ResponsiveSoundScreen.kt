package layout.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import behaviour.Sound
import com.tarifchakder.ktoast.ToastDefaults
import com.tarifchakder.ktoast.ToastHost
import com.tarifchakder.ktoast.ToastState
import layout.element.MultiWindowSizeLayout
import layout.element.Popup
import uk.thehunt.sleep.AudioPlayer

@Composable
fun ResponsiveSoundScreen(
    sounds: Map<String, List<Sound>>,
    player: AudioPlayer
) {
    var playing = player.isPlaying.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    val toast = remember { ToastState() }
    val scope = rememberCoroutineScope()

    val togglePlayback: () -> Unit = {
        if (playing.value) {
            player.pause()
        } else {
            player.resume()

//            scope.launch {
//                toast.showToast("Press and hold for more options.")
//            }
        }
    }

    val onLongClick: () -> Unit = {
//        showMenu = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        MultiWindowSizeLayout(
            default = {
                LandscapeLayout(
                    sounds,
                    player,
                    playing.value,
                    togglePlayback,
                    onLongClick
                )
            },
            expanded = {
                LandscapeLayout(
                    sounds,
                    player,
                    playing.value,
                    togglePlayback,
                    onLongClick
                )
            },
            portrait = {
                PortraitLayout(
                    sounds,
                    player,
                    playing.value,
                    togglePlayback,
                    onLongClick
                )
            },
            portraitTablet = {
                PortraitLayout(
                    sounds,
                    player,
                    playing.value,
                    togglePlayback,
                    onLongClick
                )
            },
            portraitPhone = {
                PortraitLayout(
                    sounds,
                    player,
                    playing.value,
                    togglePlayback,
                    onLongClick
                )
            }

        )

        Popup(
            show = showMenu,
            onShowChange = { showMenu = it }
        )

        ToastHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            alignment = Alignment.Center,
            hostState = toast,
            transitionSpec = { ToastDefaults.SLIDE }
        )
    }
}
