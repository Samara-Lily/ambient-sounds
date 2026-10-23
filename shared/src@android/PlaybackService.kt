package uk.thehunt.sleep

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService


class PlaybackService : MediaSessionService() {
    lateinit var mediaSession: MediaSession
    private lateinit var audioPlayer: AudioPlayer


    override fun onCreate() {
        super.onCreate()
        audioPlayer = (application as CustomApplication).audioPlayer

        val player = ExoPlayer.Builder(this).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            addListener(object : Player.Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    // React to play/pause here
                    if(playWhenReady){
                        audioPlayer.resume()
                    }else{
                        audioPlayer.pause()
                    }
                }
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    // isPlaying = true  → playing
                    // isPlaying = false → paused/buffering
                }
            })
        }

        mediaSession = MediaSession.Builder(this, player).build()
    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    override fun onDestroy() {
        mediaSession.player.release()
        mediaSession.release()
        super.onDestroy()
    }
}