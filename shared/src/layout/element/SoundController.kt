package layout.element

import androidx.compose.foundation.Image
import behaviour.Sound
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import uk.thehunt.sleep.AudioPlayer
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min


fun Color.withLightness(lightness: Float): Color {
    val r: Float = red
    val g: Float = green
    val b: Float = blue

    val maxChannel = max(r, max(g, b))
    val minChannel = min(r, min(g, b))
    val delta = maxChannel - minChannel

    val l = (maxChannel + minChannel) / 2f

    if (delta == 0f) {
        return Color(
            red = l.coerceIn(0f, 1f),
            green = l.coerceIn(0f, 1f),
            blue = l.coerceIn(0f, 1f),
            alpha = alpha
        )
    }

    val s: Float = delta / (1f - abs(2f * l - 1f))

    val h: Float = when (maxChannel) {
        r -> 60f * (((g - b) / delta) % 6f)
        g -> 60f * ((b - r) / delta + 2f)
        else -> 60f * ((r - g) / delta + 4f)
    }.let { if (it < 0f) it + 360f else it }

    val newL = lightness.coerceIn(0f, 1f)
    val c = (1f - abs(2f * newL - 1f)) * s
    val x = c * (1f - abs((h / 60f) % 2f - 1f))
    val m = newL - c / 2f

    val (rr, gg, bb) = when {
        h < 60f -> Triple(c, x, 0f)
        h < 120f -> Triple(x, c, 0f)
        h < 180f -> Triple(0f, c, x)
        h < 240f -> Triple(0f, x, c)
        h < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }

    return Color(
        red = (rr + m).coerceIn(0f, 1f),
        green = (gg + m).coerceIn(0f, 1f),
        blue = (bb + m).coerceIn(0f, 1f),
        alpha = alpha
    )
}
fun Color.withSaturation(saturation: Float): Color {
    val r: Float = red
    val g: Float = green
    val b: Float = blue

    val maxChannel = max(r, max(g, b))
    val minChannel = min(r, min(g, b))
    val delta = maxChannel - minChannel

    val l = (maxChannel + minChannel) / 2f

    if (delta == 0f) {
        return Color(
            red = l,
            green = l,
            blue = l,
            alpha = alpha
        )
    }

    val s = delta / (1f - abs(2f * l - 1f))

    val h = when (maxChannel) {
        r -> 60f * (((g - b) / delta) % 6f)
        g -> 60f * ((b - r) / delta + 2f)
        else -> 60f * ((r - g) / delta + 4f)
    }.let { if (it < 0f) it + 360f else it }

    val newS = saturation.coerceIn(0f, 1f)
    val c = (1f - abs(2f * l - 1f)) * newS
    val x = c * (1f - abs((h / 60f) % 2f - 1f))
    val m = l - c / 2f

    val (rr, gg, bb) = when {
        h < 60f -> Triple(c, x, 0f)
        h < 120f -> Triple(x, c, 0f)
        h < 180f -> Triple(0f, c, x)
        h < 240f -> Triple(0f, x, c)
        h < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }

    return Color(
        red = (rr + m).coerceIn(0f, 1f),
        green = (gg + m).coerceIn(0f, 1f),
        blue = (bb + m).coerceIn(0f, 1f),
        alpha = alpha
    )
}

private const val CURVE = 4.60517f // ln(100)

fun sliderToVolume(position: Float): Float {
    val x = position.coerceIn(0f, 1f)
    return ((exp(CURVE * x) - 1f) /
            (exp(CURVE) - 1f)).coerceIn(0f, 1f)
}

fun volumeToSlider(volume: Float): Float {
    val v = volume.coerceIn(0f, 1f)
    return (ln(1f + v * (exp(CURVE) - 1f)) /
            CURVE).coerceIn(0f, 1f)
}

@Composable
fun SoundController(
    player: AudioPlayer,
    sound: Sound
) {


    var sliderValue by remember {
        mutableStateOf(
            volumeToSlider(player.getTrackVolume(sound.path))
        )
    }

    val enabled = true

    val colors = SliderDefaults.colors(
        thumbColor = sound.color.withLightness(0.70f),
        activeTrackColor = sound.color.withLightness(0.30f).withSaturation(0.2f),
        inactiveTrackColor = sound.color.withLightness(0.30f).withSaturation(0.2f)
    )
    val backgroundColor = sound.color
    val borderColor = sound.color.withLightness(0.1f).withSaturation(0.2f)
    val textColor = sound.color.withLightness(0.90f)

    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = Modifier
            .border(2.dp, borderColor, shape)
            .fillMaxSize()
            .clip(shape)){
        sound.cover?.let {
            Image(
                painter = painterResource(it),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
        Box(
            modifier = Modifier
                .background(Color(0f, 0f, 0f, 0.4f), shape)
                .matchParentSize()
        )
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = sound.label,
            color = textColor,
            fontWeight = Bold,
            modifier = Modifier.padding(4.dp)
        )

        Column(modifier = Modifier.padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    value = sliderValue,
                    onValueChange = { newPosition ->
                        sliderValue = newPosition.coerceIn(0f, 1f)
                        if (sliderValue < 0.1f) sliderValue = 0f

                        val newVolume = sliderToVolume(sliderValue)
                        sound.volume = newVolume

                        player.setTrackVolume(sound.path, newVolume)
                        if (player.isTrackPlaying(sound.path)) {
                            if (sound.volume == 0f) {
                                player.pauseTrack(sound.path)
                            }
                        } else {
                            sound.volume?.let {
                                if (it > 0f && player.isPlaying.value) {
                                    player.resumeTrack(sound.path)
                                }
                            }
                        }

                    },
                    track = { sliderState ->
                        SliderDefaults.Track(
                            sliderState = sliderState,
                            thumbTrackGapSize = 0.dp, // Removes the 6.dp gap on sides
                            colors = colors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    },
                    enabled = enabled,
                    colors = colors,
                    valueRange = 0f..1f
                )
            }
        }
    }
    }
}