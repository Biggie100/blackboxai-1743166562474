package com.advancedquiz.social

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.advancedquiz.R

class SoundManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    @Inject lateinit var soundPreferences: SoundPreferences

    suspend fun playReactionSound(emoji: String) {
        try {
            val soundsEnabled = soundPreferences.soundsEnabled.first()
            val volume = soundPreferences.soundVolume.first()
            
            if (!soundsEnabled) return

            val soundRes = when {
                emoji.contains("❤") || emoji.contains("♥") -> R.raw.heart_sound
                emoji.contains("😂") || emoji.contains("😆") -> R.raw.laugh_sound
                emoji.contains("👍") || emoji.contains("👎") -> R.raw.thumbs_sound
                else -> R.raw.default_reaction_sound
            }
            
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, soundRes).apply {
                setVolume(volume, volume)
                start()
                setOnCompletionListener { release() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    return remember { SoundManager(context) }
}