package com.random.example.wastesorterapp.ui.result

import WasteClassificationRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.graphics.Bitmap
import com.random.example.wastesorterapp.model.WasteClassification

class ResultViewModel(
    private val classificationRepository: WasteClassificationRepository
) : ViewModel() {

    private val _classificationResult = MutableLiveData<WasteClassification>()
    val classificationResult: LiveData<WasteClassification> = _classificationResult

    fun classifyImage(bitmap: Bitmap) {
        // Use repository to classify image
        val result = classificationRepository.classifyImage(bitmap)
        _classificationResult.value = result
    }
}