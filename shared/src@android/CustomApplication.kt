package uk.thehunt.sleep

import android.app.Application

class CustomApplication : Application() {

    lateinit var audioPlayer: AudioPlayer
        private set

    override fun onCreate() {
        super.onCreate()
        audioPlayer = AudioPlayer.init(applicationContext)
    }
}