package com.random.example.wastesorterapp.ui.result

import com.random.example.wastesorterapp.databinding.ResultsDialogBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.random.example.wastesorterapp.R


class ResultBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: ResultsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ResultsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun getTheme(): Int {
        return R.style.NoBackgroundDialogTheme
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve classification result from arguments
        val wasteType = arguments?.getString("wasteType") ?: "Unknown"
        val confidence = arguments?.getInt("confidence") ?: 0
        val instructions = arguments?.getString("instructions") ?: "No specific instructions available."
        // Update UI elements
        binding.tvWasteType.text = wasteType
        binding.tvConfidencePercentage.text = "$confidence%"
        when (wasteType) {
            "Recyclable" -> {
                binding.tvWasteType.setTextColor(ContextCompat.getColor(requireContext(), R.color.recyclable))
                binding.tvWasteType.text = wasteType + " ♻️"
            }
            "Compost" -> {
                binding.tvWasteType.setTextColor(ContextCompat.getColor(requireContext(), R.color.compost))
                binding.tvWasteType.text = wasteType + "️ \uD83C\uDF31"
            }
            "Landfill"-> {
                binding.tvWasteType.setTextColor(ContextCompat.getColor(requireContext(), R.color.landfill))
                binding.tvWasteType.text = wasteType + " \uD83D\uDDD1\uFE0F️"
            }
        }
        binding.tvInstructions.text = instructions
    }

    companion object {
        fun newInstance(
            wasteType: String,
            confidence: Int,
            instructions: String
        ): ResultBottomSheetDialog {
            val fragment = ResultBottomSheetDialog()
            val args = Bundle().apply {
                putString("wasteType", wasteType)
                putInt("confidence", confidence)
                putString("instructions", instructions)
            }
            fragment.arguments = args
            return fragment
        }
    }
}