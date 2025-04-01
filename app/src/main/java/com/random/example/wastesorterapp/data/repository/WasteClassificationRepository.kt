import android.graphics.Bitmap
import com.random.example.wastesorterapp.model.WasteClassification
import com.random.example.wastesorterapp.utils.ClassifierUtils

class WasteClassificationRepository(
    private val classifierUtils: ClassifierUtils
) {
    fun classifyImage(bitmap: Bitmap): WasteClassification {
        return classifierUtils.classify(bitmap)
    }
}