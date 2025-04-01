package com.random.example.wastesorterapp.model

data class WasteClassification(
    val wasteType: WasteType,
    val confidencePercentage: Int,
    val instructions: String
) {
    enum class WasteType {
        COMPOST, RECYCLABLE, LANDFILL
    }

    companion object {
        fun getInstructions(wasteType: WasteType): String {
            return when (wasteType) {
                WasteType.COMPOST -> "Place in the green compost bin.\nRemove any non-organic materials."
                WasteType.RECYCLABLE -> "Place in the blue recycling bin.\nEnsure it's clean and dry."
                WasteType.LANDFILL -> "Place in the black trash bin.\nNo further sorting required."
            }
        }
    }
}
