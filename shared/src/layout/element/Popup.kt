package layout.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import uk.thehunt.sleep.resources.Res
import uk.thehunt.sleep.resources.pause
import uk.thehunt.sleep.resources.play

@Composable
fun Popup(
    show: Boolean,
    onShowChange: (Boolean) -> Unit
) {
    var sliderValue by remember {
        mutableStateOf(volumeToSlider(1f))
    }
    val colors = SliderDefaults.colors(
        thumbColor = Color.Gray.withLightness(0.70f),
        activeTrackColor = Color.Gray.withLightness(0.30f).withSaturation(0.2f),
        inactiveTrackColor = Color.Gray.withLightness(0.30f).withSaturation(0.2f)
    )
    Box (
    )
    {
        DropdownMenu(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(Color.Black)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp)),
            expanded = show,
            onDismissRequest = {onShowChange(false)}
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Presets",
                        color = Color.White
                    )
                },
                onClick = {
                    onShowChange(false)
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.play),
                        contentDescription = "Play/Pause",
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp),
                        tint = Color.White
                    )
                },
                enabled = true
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "Master Volume",
                        color = Color.White
                    )
                },
                onClick = {
                    onShowChange(false)
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.play),
                        contentDescription = "Play/Pause",
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp),
                        tint = Color.White
                    )
                },
                enabled = true
            )
            Text(
                "Master Volume",
                color = Color.White,
            )
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                value = sliderValue,
                onValueChange = { newPosition ->
                    sliderValue = newPosition.coerceIn(0f, 1f)
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
                enabled = true,
                colors = colors,
                valueRange = 0f..1f
            )
        }
    }
}