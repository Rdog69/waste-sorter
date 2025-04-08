package com.random.example.wastesorterapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {

    fun fixImageRotation(context: Context, imagePath: String, bitmap: Bitmap): Bitmap {
        try {
            val exif: ExifInterface = if (imagePath.startsWith("content://")) {
                // Handle gallery images
                val inputStream = context.contentResolver.openInputStream(Uri.parse(imagePath))
                inputStream?.let { ExifInterface(it) } ?: return bitmap
            } else {
                // Handle file-based images (captured from the camera)
                ExifInterface(imagePath)
            }

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
    fun getCroppedImage(originalBitmap: Bitmap): Bitmap {
        // Get dimensions for center crop
        val width = originalBitmap.width
        val height = originalBitmap.height
        val size = Math.min(width, height)

        // Calculate starting x and y coordinates for the crop
        val startX = (width - size) / 2
        val startY = (height - size) / 2

        // First center crop to square
        val croppedBitmap = Bitmap.createBitmap(
            originalBitmap,
            startX,
            startY,
            size,
            size
        )

        // Then resize to 224x224
        return Bitmap.createScaledBitmap(croppedBitmap, 224, 224, true)
    }
    fun saveProcessedImage(bitmap: Bitmap, context: Context): String {
        val filename = "processed_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)

        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }
}