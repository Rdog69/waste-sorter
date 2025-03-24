package com.random.example.wastesorterapp.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log

object ImageUtils {

     fun fixImageRotation(imagePath: String, bitmap: Bitmap): Bitmap {
        try {
            val exif = ExifInterface(imagePath)
            val rotation = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
            return if (rotation != 0) {
                val matrix = Matrix()
                matrix.postRotate(rotation.toFloat())
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap // No rotation needed
            }
        } catch (e: Exception) {
            Log.e("ImageFix", "Failed to read EXIF data", e)
            return bitmap // Return original bitmap in case of failure
        }
    }
}