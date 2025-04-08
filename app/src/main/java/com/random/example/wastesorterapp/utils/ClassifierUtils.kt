package com.random.example.wastesorterapp.utils

import android.content.Context
import android.graphics.Bitmap
import com.random.example.wastesorterapp.model.WasteClassification
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ClassifierUtils(context: Context) {
    private var interpreter: Interpreter

    init {
        val model = loadModelFile(context)
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(context: Context): ByteBuffer {
        val assetFileDescriptor = context.assets.openFd("ml/mobilenetv2_trash_classifier.tflite")
        val inputStream = assetFileDescriptor.createInputStream()
        val buffer = ByteArray(assetFileDescriptor.length.toInt())
        inputStream.read(buffer)
        inputStream.close()
        return ByteBuffer.allocateDirect(buffer.size).apply {
            order(ByteOrder.nativeOrder())
            put(buffer)
        }
    }

    fun classify(bitmap: Bitmap): WasteClassification {
        // Preprocess image
        val processedImage = preprocessImage(bitmap)

        // Prepare input & output buffers
        val inputBuffer = processedImage.buffer
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)

        // Run inference
        interpreter.run(inputBuffer, outputBuffer.buffer)

        // Get prediction results
        return postProcessResult(outputBuffer.floatArray)
    }
    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)) // Adjust size based on your model
            .add(NormalizeOp(127.5f, 127.5f)) // Scales to [-1, 1]
            .build()
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        return imageProcessor.process(tensorImage)
    }

    private fun postProcessResult(output: FloatArray): WasteClassification {
        val categories = arrayOf("COMPOST", "LANDFILL", "RECYCLABLE") // Adjust based on your model
        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1

        // Get the waste type as an enum
        val wasteType = WasteClassification.WasteType.valueOf(categories[maxIndex])

        // Calculate confidence percentage
        val confidencePercentage = (output[maxIndex] * 100).toInt()

        // Get instructions for this waste type
        val instructions = WasteClassification.getInstructions(wasteType)

        return WasteClassification(wasteType, confidencePercentage, instructions)
    }
}