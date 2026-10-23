package uk.thehunt.sleep

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import uk.thehunt.sleep.resources.Res
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.jvm.java
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


actual class AudioPlayer actual constructor() {

    val players = mutableMapOf<String, ExoPlayer>()
    private val _isPlaying = MutableStateFlow(false)
    actual val isPlaying: StateFlow<Boolean> =
        _isPlaying.asStateFlow()
    companion object {
        @Volatile
        var instance: AudioPlayer? = null

        fun init(context: Context): AudioPlayer {
            return instance ?: synchronized(this) {
                instance ?: AudioPlayer(context.applicationContext).also { instance = it }
            }
        }

        fun get(): AudioPlayer =
            instance ?: error("AudioPlayer not initialised. Call AudioPlayer.init(context) first.")

        fun reset() {
            instance?.stop()
            instance = null
        }
    }

    private lateinit var appContext: Context
    @OptIn(ExperimentalAtomicApi::class)
    private val nextHandle = AtomicLong(0L)
    private var released = false

    private constructor(appContext: Context) : this() {
        this.appContext = appContext
    }

    @OptIn(ExperimentalAtomicApi::class)
    actual fun playTrack(path: String): String {
        check(!released) { "AudioPlayer has been released" }
        val player = ExoPlayer.Builder(appContext).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 1f
            setMediaItem(MediaItem.Builder()
                .setUri(Res.getUri(path))
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtworkUri(Res.getUri("drawable/cover.jpg").toUri())
                        .build()
                )
                .build())
            prepare()
        }
        players[path] = player
        return path
    }

    actual fun stopTrack(handle: String) {
        players.remove(handle)?.let {
            it.stop()
            it.release()
        }
    }

    actual fun resumeTrack(handle: String) {
        players[handle]?.play()
    }

    actual fun pauseTrack(handle: String) {
        players[handle]?.pause()
    }

    actual fun setTrackVolume(handle: String, volume: Float) {
        players[handle]?.volume = volume.coerceIn(0f, 1f)
    }
    actual fun getTrackVolume(handle: String): Float {
        return if(players[handle] == null) 0f else {
            players[handle]!!.volume
        }
    }

    actual fun stop() {
        if (released) return
        released = true
        players.values.forEach {
            it.stop()
            it.release()
        }
        players.clear()
    }

    actual fun resume() {
        players.forEach { (_, player) ->
            player.play()
        }

        _isPlaying.value = true

        val token = SessionToken(
            appContext,
            ComponentName(appContext, PlaybackService::class.java)
        )

        val controllerFuture =
            MediaController.Builder(appContext, token).buildAsync()

        controllerFuture.addListener({
            val controller = controllerFuture.get()

            controller.setMediaItem(
                MediaItem.fromUri(Res.getUri("files/silence.mp3"))
            )
            controller.prepare()
            controller.play()
        }, ContextCompat.getMainExecutor(appContext))
    }

    actual fun pause() {
        players.forEach { (_, player) ->
            player.pause()
        }

        _isPlaying.value = false

        val token = SessionToken(
            appContext,
            ComponentName(appContext, PlaybackService::class.java)
        )

        val controllerFuture =
            MediaController.Builder(appContext, token).buildAsync()

        controllerFuture.addListener({
            val controller = controllerFuture.get()
            controller.pause()
            controller.release()
        }, ContextCompat.getMainExecutor(appContext))
    }

    actual var mode: PLAYBACK_MODE = PLAYBACK_MODE.MIXDOWN


    actual fun isTrackPlaying(handle: String): Boolean {
        return players[handle] != null && players[handle]!!.isPlaying
    }
}