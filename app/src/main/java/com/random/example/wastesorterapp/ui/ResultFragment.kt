package com.random.example.wastesorterapp.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val classifier = ClassifierUtils(requireContext())

        binding = FragmentResultBinding.inflate(inflater)
        val imagePath = arguments?.getString("imagePath")
        if (imagePath != null) {
            val bitmap = if (imagePath.startsWith("content://")) {
                val inputStream = requireContext().contentResolver.openInputStream(Uri.parse(imagePath))
                BitmapFactory.decodeStream(inputStream)
            } else {
                BitmapFactory.decodeFile(imagePath)
            }

            val correctedBitmap = ImageUtils.fixImageRotation(requireContext(), imagePath, bitmap!!)

            // Get the cropped bitmap used for classification
            val croppedBitmap = classifier.getCroppedImage(correctedBitmap)

            val classificationResult = classifier.classify(correctedBitmap)
            binding.tvResult.text = "This item is: $classificationResult"

            // Show both the original and cropped images
            binding.ivCaptured.setImageBitmap(correctedBitmap)
            binding.ivCroppedImage.setImageBitmap(croppedBitmap)
            binding.ivCroppedImage.visibility = View.VISIBLE
        }

        binding.close.setOnClickListener{
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}