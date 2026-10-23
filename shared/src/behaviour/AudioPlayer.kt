package uk.thehunt.sleep

import kotlinx.coroutines.flow.StateFlow

enum class PLAYBACK_MODE {
    MIXDOWN, // Takes in a series of loops, mixes them down and plays them at once. Shows no track seek controls.
    DETERMINATE // Plays a single track and enables seeking controls.
}
expect class AudioPlayer() {
    var mode: PLAYBACK_MODE

    // Mixdown mode playback options:
    fun playTrack(path: String): String
    fun resumeTrack(handle: String)
    fun pauseTrack(handle: String)
    fun stopTrack(handle: String)
    fun getTrackVolume(handle: String): Float
    fun isTrackPlaying(handle: String): Boolean
    fun setTrackVolume(handle: String, volume: Float)
    fun resume()
    fun pause()
    fun stop()


    val isPlaying: StateFlow<Boolean>
}