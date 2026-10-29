package com.app.screentime.feature.preferences

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

/**
 * ActivityResultContract that explicitly requests the FRONT (selfie) camera across
 * various Android OEMs (Google Pixel, Samsung, Xiaomi, OnePlus, Vivo, Oppo, Motorola).
 */
class FrontCameraTakePictureContract : ActivityResultContract<Uri, Boolean>() {

    override fun createIntent(context: Context, input: Uri): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, input)

            // Universal OEM front-facing camera flags:
            // 1. Android standard & AOSP
            putExtra("android.intent.extras.CAMERA_FACING", 1) // 1 = Front, 0 = Back
            putExtra("android.intent.extras.LENS_FACING_FRONT", 1)

            // 2. Samsung & OneUI
            putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
            putExtra("camerafacing", "front")

            // 3. Google Pixel & Google Camera
            putExtra("com.google.assistant.extra.USE_FRONT_CAMERA", true)
            putExtra("android.intent.extra.CAMERA_FACING_FRONT", 1)

            // 4. Xiaomi / MIUI / HyperOS
            putExtra("previous_mode", "front")
            putExtra("default_camera", "1")

            // 5. Motorola / MediaTek / Transsion
            putExtra("facing", "front")

            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
