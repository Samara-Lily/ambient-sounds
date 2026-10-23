package uk.thehunt.sleep

import kotlinx.browser.document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.w3c.dom.HTMLAudioElement
import uk.thehunt.sleep.resources.Res

actual class AudioPlayer {
    private val players = mutableMapOf<String, HTMLAudioElement>()
    private var released = false


    private val _isPlaying = MutableStateFlow(false)
    actual val isPlaying: StateFlow<Boolean> =
        _isPlaying.asStateFlow()

    actual fun playTrack(path: String): String {
        check(!released) { "AudioPlayer has been released" }

        val audio = document.createElement("audio") as HTMLAudioElement
        audio.src = Res.getUri(path)
        audio.volume = 1.0
        audio.loop = true
        audio.preload = "auto"

        players[path] = audio
        return path
    }

    actual fun stopTrack(handle: String) {
        players.remove(handle)?.let { audio ->
            audio.pause()
            audio.currentTime = 0.0
            audio.removeAttribute("src")
            audio.load()
        }
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun resumeTrack(handle: String) {
        players[handle]?.play()
    }

    actual fun pauseTrack(handle: String) {
        players[handle]?.pause()
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun resume() {
        _isPlaying.value = true
        players.forEach { player ->
            player.value.play()
        }
    }

    actual fun pause() {
        _isPlaying.value = false
        players.forEach { player ->
            player.value.pause()
        }
    }


    actual fun setTrackVolume(handle: String, volume: Float) {
        players[handle]?.volume = volume.coerceIn(0f, 1f).toDouble()
    }

    actual fun stop() {
        if (released) return
        released = true

        players.values.forEach { audio ->
            audio.pause()
            audio.removeAttribute("src")
            audio.load()
        }
        players.clear()
    }

    actual var mode: PLAYBACK_MODE = PLAYBACK_MODE.MIXDOWN

    actual fun getTrackVolume(handle: String): Float {
        return if(players[handle] == null) 0f else players[handle]!!.volume.toFloat()
    }


    actual fun isTrackPlaying(handle: String): Boolean {
        return players[handle] != null && !players[handle]!!.paused
    }
}