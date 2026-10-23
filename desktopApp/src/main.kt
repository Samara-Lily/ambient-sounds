package uk.thehunt.sleep

import App
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.nucleusframework.composenativetray.tray.api.Tray

fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    Window(
        onCloseRequest = { isVisible = false },
        visible = isVisible,
        title = "Ambient Sounds",
        icon = painterResource("drawable/ic_launcher.png")
    ) {
        App(AudioPlayer())
    }
    Tray(
        icon = painterResource("drawable/ic_launcher.png"),
        tooltip = "Ambient Sounds",
        primaryAction = { isVisible = true },
    ){
        Item("Show", onClick = { isVisible = true })
        Item("Exit", onClick = ::exitApplication)
    }

}
