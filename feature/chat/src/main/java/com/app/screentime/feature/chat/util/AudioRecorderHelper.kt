package com.app.screentime.feature.chat.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File

class AudioRecorderHelper(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var currentOutputFile: File? = null
    private var isRecording = false
    private var recordStartTime: Long = 0L

    fun startRecording(): Boolean {
        return try {
            stopRecording() // Clean up any existing session
            val audioDir = File(context.cacheDir, "audio_records").apply { mkdirs() }
            val outputFile = File(audioDir, "audio_${System.currentTimeMillis()}.m4a")
            currentOutputFile = outputFile

            val newRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            newRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(44100)
                setAudioEncodingBitRate(96000)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
            }
            recorder = newRecorder
            isRecording = true
            recordStartTime = System.currentTimeMillis()
            true
        } catch (e: Exception) {
            Log.e("AudioRecorderHelper", "Failed to start recording: ${e.message}", e)
            cancelRecording()
            false
        }
    }

    fun stopRecording(): File? {
        if (!isRecording || recorder == null) return null
        return try {
            recorder?.apply {
                try {
                    stop()
                } catch (_: Exception) {}
                release()
            }
            recorder = null
            isRecording = false
            val file = currentOutputFile
            currentOutputFile = null
            if (file != null && file.exists() && file.length() > 0) {
                file
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AudioRecorderHelper", "Failed to stop recording: ${e.message}", e)
            cancelRecording()
            null
        }
    }

    fun cancelRecording() {
        try {
            recorder?.apply {
                try {
                    stop()
                } catch (_: Exception) {}
                release()
            }
        } catch (_: Exception) {}
        recorder = null
        isRecording = false
        currentOutputFile?.let {
            if (it.exists()) it.delete()
        }
        currentOutputFile = null
    }

    fun isCurrentlyRecording(): Boolean = isRecording

    fun getElapsedDurationSeconds(): Int {
        if (!isRecording) return 0
        return ((System.currentTimeMillis() - recordStartTime) / 1000).toInt()
    }
}
