package com.random.example.wastesorterapp.ui.result

import WasteClassificationRepository
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.random.example.wastesorterapp.databinding.FragmentResultBinding
import com.random.example.wastesorterapp.utils.ClassifierUtils
import com.random.example.wastesorterapp.utils.ImageUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.Locale

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding

    // Create ViewModel instance using factory the "by" delegates the viewmodel creation and makes it lazy
    // meaning only uses creates it when it needs
    private val viewModel: ResultViewModel by viewModels {
        val classifierUtils = ClassifierUtils(requireContext())
        val repository = WasteClassificationRepository(classifierUtils)
        ResultViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)

        val imagePath = arguments?.getString("imagePath")
        if (imagePath != null) {
            // Decode bitmap
            val bitmap = if (imagePath.startsWith("content://")) {
                val inputStream = requireContext().contentResolver.openInputStream(Uri.parse(imagePath))
                BitmapFactory.decodeStream(inputStream)
            } else {
                BitmapFactory.decodeFile(imagePath)
            }

            // Correct image rotation
            val correctedBitmap = ImageUtils.fixImageRotation(requireContext(), imagePath, bitmap!!)
            val croppedImage = ImageUtils.getCroppedImage(correctedBitmap)
            ImageUtils.saveProcessedImage(
                croppedImage,
                context = requireContext()
            )
            // Show original image
            binding.ivCaptured.setImageBitmap(correctedBitmap)
            // Use ViewModel to classify the cropped image
            viewModel.classifyImage(croppedImage)
        }

        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe classification results
        viewModel.classificationResult.observe(viewLifecycleOwner) { result ->
            // Show bottom sheet dialog with results
            val bottomSheetDialog = ResultBottomSheetDialog.newInstance(
                result.wasteType.name.lowercase().capitalize(),
                result.confidencePercentage,
                result.instructions
            )
            bottomSheetDialog.show(childFragmentManager, "ResultBottomSheet")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}

// Add this class to ResultViewModel.kt
class ResultViewModelFactory(
    private val repository: WasteClassificationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Helper extension function for String capitalization
fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}