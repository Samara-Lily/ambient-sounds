package layout.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import behaviour.Sound
import layout.element.MixerControls
import layout.element.SoundGrid
import uk.thehunt.sleep.AudioPlayer

@Composable
fun LandscapeLayout(
    sounds: Map<String, List<Sound>>,
    player: AudioPlayer,
    playing: Boolean,
    onTogglePlayback: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        SoundGrid(
            sounds = sounds,
            player = player,
            modifier = Modifier
                .fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            MixerControls(
                playing = playing,
                onTogglePlayback = onTogglePlayback,
                onLongClick = onLongClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
