package com.app.screentime.feature.call

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import kotlinx.coroutines.*

class AudioManagerHelper(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private var toneGenerator: ToneGenerator? = null
    private var ringbackJob: Job? = null
    private var previousAudioMode: Int = AudioManager.MODE_NORMAL

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (e: Exception) {
            Log.w("AudioHelper", "Failed to initialize tone generator: ${e.message}")
        }
    }

    fun startCallAudio() {
        try {
            audioManager?.let { am ->
                previousAudioMode = am.mode
                am.mode = AudioManager.MODE_IN_COMMUNICATION
                am.isMicrophoneMute = false
                am.isSpeakerphoneOn = false
            }
        } catch (e: Exception) {
            Log.e("AudioHelper", "Failed to start call audio: ${e.message}")
        }
    }

    fun setMuted(muted: Boolean) {
        try {
            audioManager?.isMicrophoneMute = muted
        } catch (e: Exception) {
            Log.e("AudioHelper", "Failed to set mute: ${e.message}")
        }
    }

    fun setSpeaker(speaker: Boolean) {
        try {
            audioManager?.isSpeakerphoneOn = speaker
        } catch (e: Exception) {
            Log.e("AudioHelper", "Failed to set speaker: ${e.message}")
        }
    }

    /**
     * Plays a repeating realistic outgoing ringback tone cadence while dialing.
     */
    fun startDialingTone(scope: CoroutineScope) {
        stopDialingTone()
        ringbackJob = scope.launch(Dispatchers.Default) {
            try {
                while (isActive) {
                    try {
                        toneGenerator?.startTone(ToneGenerator.TONE_SUP_RINGTONE, 1800)
                    } catch (e: Exception) {
                        Log.w("AudioHelper", "Tone error: ${e.message}")
                    }
                    delay(3800) // Standard 1.8s ring, 2s silence cadence
                }
            } catch (e: CancellationException) {
                // Cancelled when answered or hung up
            }
        }
    }

    fun stopDialingTone() {
        ringbackJob?.cancel()
        ringbackJob = null
        try {
            toneGenerator?.stopTone()
        } catch (e: Exception) {
            Log.w("AudioHelper", "Failed to stop tone: ${e.message}")
        }
    }

    /**
     * Plays a pleasant chime when the call connects.
     */
    fun playCallConnectedTone() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 400)
        } catch (e: Exception) {
            Log.w("AudioHelper", "Failed to play connect tone: ${e.message}")
        }
    }

    /**
     * Plays a standard busy/disconnect tone when the call ends.
     */
    fun playCallEndedTone() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_BUSY, 800)
        } catch (e: Exception) {
            Log.w("AudioHelper", "Failed to play end tone: ${e.message}")
        }
    }

    fun stopCallAudio() {
        stopDialingTone()
        playCallEndedTone()
        try {
            audioManager?.let { am ->
                am.mode = previousAudioMode
                am.isMicrophoneMute = false
                am.isSpeakerphoneOn = false
            }
        } catch (e: Exception) {
            Log.e("AudioHelper", "Failed to restore audio mode: ${e.message}")
        }
    }

    fun release() {
        stopDialingTone()
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            Log.w("AudioHelper", "Failed to release tone generator: ${e.message}")
        }
    }
}
