package layout.element

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import uk.thehunt.sleep.resources.Res
import uk.thehunt.sleep.resources.pause
import uk.thehunt.sleep.resources.play


@Composable
fun MixerControls(
    playing: Boolean,
    onTogglePlayback: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = vectorResource(
        if (playing) Res.drawable.pause
        else Res.drawable.play
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(20.dp)
                .clip(CircleShape)
                .background(Color(0.2f, 0.2f, 0.2f))
                .combinedClickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null,
                    onClick = onTogglePlayback,
                    onLongClick = onLongClick
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Play/Pause",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                tint = Color.White
            )
        }
    }
}
