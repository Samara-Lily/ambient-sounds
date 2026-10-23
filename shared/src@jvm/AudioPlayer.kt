
package uk.thehunt.sleep

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.thehunt.sleep.resources.Res
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.BooleanControl
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import kotlin.math.log10

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AudioPlayer {

    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val lock = Any()

    private class PlayerEntry(
        var volume: Float,
        var clip: Clip? = null,
        var wantsToPlay: Boolean = false
    )

    private val players = mutableMapOf<String, PlayerEntry>()

    private var released = false

    actual fun playTrack(path: String): String {
        val entry = PlayerEntry(
            volume = 1f
        )

        synchronized(lock) {
            check(!released) {
                "AudioPlayer has been released"
            }

            players[path] = entry
        }

        scope.launch {
            var loadedClip: Clip? = null

            try {
                val bytes = Res.readBytes(path)

                currentCoroutineContext().ensureActive()

                loadedClip = decodeToClip(bytes)

                synchronized(lock) {
                    if (released || players[path] !== entry) {
                        loadedClip.close()
                        return@synchronized
                    }

                    entry.clip = loadedClip
                    applyVolume(loadedClip, entry.volume)

                    if (entry.wantsToPlay) {
                        startClip(loadedClip)
                    }
                }
            } catch (e: CancellationException) {
                loadedClip?.close()
                throw e
            } catch (e: Exception) {
                loadedClip?.close()

                synchronized(lock) {
                    if (players[path] === entry) {
                        players.remove(path)
                    }
                }

                System.err.println(
                    "Failed to load audio resource: $path"
                )
                e.printStackTrace()
            }
        }

        return path
    }

    private fun decodeToClip(bytes: ByteArray): Clip {
        val buffered = BufferedInputStream(
            ByteArrayInputStream(bytes)
        )

        return AudioSystem.getAudioInputStream(buffered).use { source ->
            val sourceFormat = source.format

            val sampleRate = sourceFormat.sampleRate
                .takeIf { it.isFinite() && it > 0f }
                ?: 44100f

            val channels = sourceFormat.channels
                .takeIf { it == 1 || it == 2 }
                ?: 2

            val pcmFormat = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                16,
                channels,
                channels * 2,
                sampleRate,
                false
            )

            val pcmStream = if (
                sourceFormat.encoding == AudioFormat.Encoding.PCM_SIGNED &&
                sourceFormat.sampleSizeInBits == 16 &&
                sourceFormat.channels == channels &&
                sourceFormat.frameSize == channels * 2 &&
                !sourceFormat.isBigEndian
            ) {
                source
            } else {
                AudioSystem.getAudioInputStream(pcmFormat, source)
            }

            if (pcmStream === source) {
                AudioSystem.getClip().apply {
                    try {
                        open(source)
                    } catch (e: Exception) {
                        close()
                        throw e
                    }
                }
            } else {
                pcmStream.use { pcm ->
                    AudioSystem.getClip().apply {
                        try {
                            open(pcm)
                        } catch (e: Exception) {
                            close()
                            throw e
                        }
                    }
                }
            }
        }
    }

    actual fun resumeTrack(handle: String) {
        synchronized(lock) {
            val entry = players[handle] ?: return

            entry.wantsToPlay = true

            entry.clip?.let { clip ->
                startClip(clip)
            }
        }
    }

    private fun startClip(clip: Clip) {
        if (clip.framePosition >= clip.frameLength) {
            clip.framePosition = 0
        }

        clip.loop(Clip.LOOP_CONTINUOUSLY)
    }

    actual fun pauseTrack(handle: String) {
        synchronized(lock) {
            val entry = players[handle] ?: return

            entry.wantsToPlay = false
            entry.clip?.stop()
        }
    }

    actual fun stopTrack(handle: String) {
        val clip = synchronized(lock) {
            val entry = players.remove(handle) ?: return
            entry.wantsToPlay = false
            entry.clip
        }

        clip?.let {
            it.stop()
            it.close()
        }
    }

    actual fun setTrackVolume(handle: String, volume: Float) {
        synchronized(lock) {
            val entry = players[handle] ?: return

            entry.volume = volume.coerceIn(0f, 1f)

            entry.clip?.let { clip ->
                applyVolume(clip, entry.volume)
            }
        }
    }

    private fun applyVolume(clip: Clip, volume: Float) {
        val safeVolume = volume.coerceIn(0f, 1f)

        if (clip.isControlSupported(BooleanControl.Type.MUTE)) {
            val mute = clip.getControl(
                BooleanControl.Type.MUTE
            ) as BooleanControl

            mute.value = safeVolume <= 0f
        }

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            val gain = clip.getControl(
                FloatControl.Type.MASTER_GAIN
            ) as FloatControl

            val decibels = if (safeVolume <= 0f) {
                gain.minimum
            } else {
                (20.0 * log10(safeVolume.toDouble()))
                    .toFloat()
                    .coerceIn(gain.minimum, gain.maximum)
            }

            gain.value = decibels
        }
    }

    actual fun stop() {
        val clips = synchronized(lock) {
            if (released) return

            released = true

            val activeClips = players.values.mapNotNull {
                it.clip
            }

            players.clear()
            activeClips
        }

        scope.cancel()

        clips.forEach { clip ->
            clip.stop()
            clip.close()
        }
    }

    actual fun resume(){
        _isPlaying.value = true
        players.forEach { player ->
            synchronized(lock) {
                player.value.wantsToPlay = true
                player.value.clip?.let { clip ->
                    startClip(clip)
                }
            }
        }
    }
    actual fun pause(){
        _isPlaying.value = false
        players.forEach { player ->
            synchronized(lock) {
                player.value.wantsToPlay = false
                player.value.clip?.stop()
            }
        }
    }

    actual var mode: PLAYBACK_MODE = PLAYBACK_MODE.MIXDOWN

    actual fun getTrackVolume(handle: String): Float {
        return if(players[handle] != null) players[handle]!!.volume else 0f
    }

    actual fun isTrackPlaying(handle: String): Boolean {
        return players[handle] != null && players[handle]!!.wantsToPlay
    }



    private val _isPlaying = MutableStateFlow(false)
    actual val isPlaying: StateFlow<Boolean> =
        _isPlaying.asStateFlow()

}
