package layout.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
fun SoundGrid(
    sounds: Map<String, List<Sound>>,
    player: AudioPlayer,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .padding(5.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(when {
                maxWidth < 600.dp -> 1
                maxWidth < 1000.dp -> 2
                maxWidth < 1200.dp -> 3
                else -> 4
            }),
            horizontalArrangement = Arrangement.spacedBy(5.dp), // Gap between columns
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item(key = "main_title", span = { GridItemSpan(maxLineSpan) }) {
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
                item(key = "header_$category", span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 8.dp)
                            .width(512.dp)

                    )
                }

                itemsIndexed(
                    items = soundList,
                    key = { _, sound -> sound.path },
                ) { index, sound ->
                    SoundController(
                        player = player,
                        sound = sound
                    )
                }
            }

            item("footer_gap", span = { GridItemSpan(maxLineSpan) }){
                Spacer(Modifier.height(100.dp))
            }
        }
    }
}
