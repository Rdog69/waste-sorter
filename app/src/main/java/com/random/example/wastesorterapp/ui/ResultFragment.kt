package com.random.example.wastesorterapp.ui

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
import com.random.example.wastesorterapp.utils.ImageUtils


class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            binding.ivCaptured.setImageBitmap(correctedBitmap)
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