import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import behaviour.Sound
import layout.screen.ResponsiveSoundScreen
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import uk.thehunt.sleep.AudioPlayer
import uk.thehunt.sleep.resources.Res
import uk.thehunt.sleep.resources.cover_babbling_brook
import uk.thehunt.sleep.resources.cover_birds
import uk.thehunt.sleep.resources.cover_cafe_ambience
import uk.thehunt.sleep.resources.cover_cicadas
import uk.thehunt.sleep.resources.cover_fan_noise
import uk.thehunt.sleep.resources.cover_fireplace
import uk.thehunt.sleep.resources.cover_rain_on_leaves
import uk.thehunt.sleep.resources.cover_rain_on_tin
import uk.thehunt.sleep.resources.cover_rain_on_window
import uk.thehunt.sleep.resources.cover_seagulls
import uk.thehunt.sleep.resources.cover_thunder
import uk.thehunt.sleep.resources.cover_urban_rain
import uk.thehunt.sleep.resources.cover_waves
import uk.thehunt.sleep.resources.cover_wind

@Composable
@DevelopmentEntryPoint
fun App(player: AudioPlayer) {
    val sounds = remember {
        mapOf(
            "Wildlife" to listOf(
                Sound("Bird Song", "files/Birds.mp3",
                    color = Color(0.0f, 1.0f, 1.0f, 1.0f),
                    cover = Res.drawable.cover_birds),
                Sound("Cicadas", "files/Cicadas.mp3",
                    color = Color(0.0f, 0.925f, 0.267f, 1.0f),
                    cover = Res.drawable.cover_cicadas
                ),
                Sound("Seagulls", "files/Seagulls.mp3",
                    color = Color(0.58f, 0.58f, 0.843f, 1.0f),
                    cover = Res.drawable.cover_seagulls)
            ),
            "Nature" to listOf(
                Sound("Babbling Brook", "files/Babbling Brook.mp3", color = Color(
                    0.31f,
                    1.0f,
                    1.0f,
                    1.0f
                ),
                    cover = Res.drawable.cover_babbling_brook
                ),
                Sound("Wind", "files/Wind.mp3",
                    color = Color(0.6f, 0.678f, 0.678f, 1.0f),
                    cover = Res.drawable.cover_wind),
                Sound("Thunder", "files/Thunder.mp3",
                    color = Color(1.0f, 0.8f, 0.0f, 1.0f),
                    cover = Res.drawable.cover_thunder),
                Sound("Ocean Waves", "files/Waves.mp3",
                    color = Color(0.063f, 0.612f, 0.42f, 1.0f),
                    cover = Res.drawable.cover_waves)
            ),
            "Rain" to listOf(
                Sound("Rain On Tin Roof", "files/Tin Rain.mp3", color = Color(
                    0.0f,
                    0.4f,
                    1.0f,
                    1.0f
                ),
                    cover = Res.drawable.cover_rain_on_tin
                ),
                Sound("Urban Rain", "files/Urban Rain.mp3", color = Color(
                    0.161f,
                    0.733f,
                    0.733f,
                    1.0f
                ),
                    cover = Res.drawable.cover_urban_rain
                ),
                Sound("Rain On Leaves", "files/Leaf Rain.mp3", color = Color(
                    0.0f,
                    0.659f,
                    0.506f,
                    1.0f
                ),
                    cover = Res.drawable.cover_rain_on_leaves
                ),
                Sound("Rain On Window", "files/Window Rain.mp3",
                    color = Color(0.0f, 1.0f, 1.0f, 1.0f),
                    cover = Res.drawable.cover_rain_on_window),
            ),
            "Indoor Comforts" to listOf(
                Sound("Fan Noise", "files/Fan Noise.mp3", color = Color(
                    0.498f,
                    0.275f,
                    0.075f,
                    1.0f
                ),
                    cover = Res.drawable.cover_fan_noise
                ),
                Sound("Crackling Fire", "files/Fireplace.mp3", color = Color(
                    1.0f,
                    0.549f,
                    0.0f,
                    1.0f
                ),
                    cover = Res.drawable.cover_fireplace
                ),
                Sound("Cafe Ambience", "files/Cafe.mp3",
                    color = Color(0.584f, 0.0f, 1.0f, 1.0f),
                    cover = Res.drawable.cover_cafe_ambience
                )
            )
        )
    }

    MaterialTheme {
        LaunchedEffect(player) {
            sounds.forEach { category ->
                category.value.forEach { sound ->
                    if(!player.isTrackPlaying(sound.path)) {
                        player.playTrack(
                            sound.path
                        )
                        player.setTrackVolume(sound.path, sound.volume ?: 0f)
                    }
                }
            }
        }
        ResponsiveSoundScreen(
            sounds = sounds,
            player = player
        )
    }

}