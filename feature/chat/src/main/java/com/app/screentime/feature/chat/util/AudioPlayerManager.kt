package com.app.screentime.feature.chat.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.app.screentime.core.network.security.E2ECryptoManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

object AudioPlayerManager {
    private const val TAG = "AudioPlayerManager"
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressJob: Job? = null

    private val _currentlyPlayingId = MutableStateFlow<String?>(null)
    val currentlyPlayingId: StateFlow<String?> = _currentlyPlayingId.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoadingAudio = MutableStateFlow<String?>(null)
    val isLoadingAudio: StateFlow<String?> = _isLoadingAudio.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _currentPositionSec = MutableStateFlow(0)
    val currentPositionSec: StateFlow<Int> = _currentPositionSec.asStateFlow()

    var onAudioCompleted: ((messageId: String) -> Unit)? = null

    fun togglePlay(
        context: Context,
        messageId: String,
        audioUrlOrPath: String,
        otherUserId: String? = null,
        e2eCryptoManager: E2ECryptoManager? = null,
        token: String? = null
    ) {
        if (_currentlyPlayingId.value == messageId && _isPlaying.value) {
            pause()
        } else if (_currentlyPlayingId.value == messageId && mediaPlayer != null) {
            resume()
        } else {
            resolveAndPlay(context, messageId, audioUrlOrPath, otherUserId, e2eCryptoManager, token)
        }
    }

    fun togglePlay(messageId: String, audioUrlOrPath: String) {
        if (_currentlyPlayingId.value == messageId && _isPlaying.value) {
            pause()
        } else if (_currentlyPlayingId.value == messageId && mediaPlayer != null) {
            resume()
        } else {
            playNew(messageId, audioUrlOrPath)
        }
    }

    private fun resolveAndPlay(
        context: Context,
        messageId: String,
        audioUrlOrPath: String,
        otherUserId: String?,
        e2eCryptoManager: E2ECryptoManager?,
        token: String?
    ) {
        stop()

        // 1. If it's already a local file path that exists, play directly
        if (!audioUrlOrPath.startsWith("http://", ignoreCase = true) &&
            !audioUrlOrPath.startsWith("https://", ignoreCase = true)
        ) {
            val localFile = File(audioUrlOrPath)
            if (localFile.exists() && localFile.length() > 0) {
                playNew(messageId, localFile.absolutePath)
                return
            }
        }

        // 2. Remote URL: check local decrypted cache
        val cacheDir = File(context.cacheDir, "e2e_audio").apply { mkdirs() }
        val safeHash = audioUrlOrPath.hashCode().toUInt().toString(16)
        val cachedFile = File(cacheDir, "dec_$safeHash.m4a")

        if (cachedFile.exists() && cachedFile.length() > 0) {
            playNew(messageId, cachedFile.absolutePath)
            return
        }

        // 3. Download, decrypt with E2EE, and cache
        _isLoadingAudio.value = messageId
        scope.launch(Dispatchers.IO) {
            try {
                val url = java.net.URL(audioUrlOrPath)
                val rawBytes = url.openStream().use { it.readBytes() }

                val decryptedBytes = if (e2eCryptoManager != null && !otherUserId.isNullOrBlank() && !token.isNullOrBlank()) {
                    e2eCryptoManager.decryptAudioBytes(rawBytes, otherUserId, token)
                } else {
                    rawBytes
                }

                cachedFile.writeBytes(decryptedBytes)

                withContext(Dispatchers.Main) {
                    _isLoadingAudio.value = null
                    playNew(messageId, cachedFile.absolutePath)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to download and decrypt audio from $audioUrlOrPath: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _isLoadingAudio.value = null
                    stop()
                }
            }
        }
    }

    private fun playNew(messageId: String, audioUrlOrPath: String) {
        stop()
        _currentlyPlayingId.value = messageId
        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrlOrPath)
                setOnPreparedListener { mp ->
                    mp.start()
                    _isPlaying.value = true
                    startProgressTracker(mp)
                }
                setOnCompletionListener {
                    val completedId = _currentlyPlayingId.value
                    stop()
                    if (!completedId.isNullOrBlank()) {
                        onAudioCompleted?.invoke(completedId)
                    }
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                    stop()
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start audio playback for $audioUrlOrPath: ${e.message}", e)
            stop()
        }
    }

    fun pause() {
        try {
            mediaPlayer?.pause()
            _isPlaying.value = false
            progressJob?.cancel()
        } catch (_: Exception) {}
    }

    fun resume() {
        try {
            mediaPlayer?.start()
            _isPlaying.value = true
            mediaPlayer?.let { startProgressTracker(it) }
        } catch (_: Exception) {}
    }

    fun stop() {
        progressJob?.cancel()
        progressJob = null
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
        } catch (_: Exception) {}
        mediaPlayer = null
        _currentlyPlayingId.value = null
        _isPlaying.value = false
        _isLoadingAudio.value = null
        _playbackProgress.value = 0f
        _currentPositionSec.value = 0
    }

    private fun startProgressTracker(player: MediaPlayer) {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive && _isPlaying.value) {
                try {
                    val current = player.currentPosition
                    val total = player.duration
                    if (total > 0) {
                        _playbackProgress.value = (current.toFloat() / total.toFloat()).coerceIn(0f, 1f)
                        _currentPositionSec.value = (current / 1000)
                    }
                } catch (_: Exception) {}
                delay(100)
            }
        }
    }
}
