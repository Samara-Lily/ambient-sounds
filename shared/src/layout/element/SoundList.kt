package layout.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import behaviour.Sound
import uk.thehunt.sleep.AudioPlayer


@Composable
fun SoundList(
    sounds: Map<String, List<Sound>>,
    player: AudioPlayer,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        item(key = "main_title") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(
                            top = 30.dp,
                            bottom = 14.dp
                        )
                ){
                    Text(
                        text = "Ambient Sounds",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        sounds.forEach { (category, soundList) ->
            item(key = "header_$category") {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(
                        top = 30.dp,
                        bottom = 14.dp
                    )
                )
            }

            itemsIndexed(
                items = soundList,
                key = { _, sound -> sound.path }
            ) { index, sound ->
                SoundController(
                    player = player,
                    sound = sound
                )

                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        item("footer_gap"){
            Spacer(Modifier.height(100.dp))
        }
    }
}
