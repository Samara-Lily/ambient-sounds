package behaviour

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

class Sound(val label: String, val path: String, var volume: Float? = null, val color: Color = Color(0f, 1f, 0.5f), val cover: DrawableResource? = null)