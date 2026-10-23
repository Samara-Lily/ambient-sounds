package uk.thehunt.sleep

import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSURL
import uk.thehunt.sleep.resources.Res

@OptIn(ExperimentalResourceApi::class)
actual class AudioPlayer {
    @OptIn(ExperimentalForeignApi::class)
    actual fun playSound(path: String) {
        val uri = Res.getUri(path)
        NSURL.URLWithString(URLString = uri)?.let { media ->
            val avAudioPlayer = AVAudioPlayer(media, error = null)
            avAudioPlayer.prepareToPlay()
            avAudioPlayer.play()
        }
    }
    actual fun stop() {}
    actual fun setVolume(volume: Float) {}
}