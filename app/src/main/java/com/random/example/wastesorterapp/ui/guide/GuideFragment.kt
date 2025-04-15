package com.random.example.wastesorterapp.ui.guide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.random.example.wastesorterapp.R
import com.random.example.wastesorterapp.model.WasteClassification

class GuideFragment : Fragment() {

    private lateinit var recyclableLayout: LinearLayout
    private lateinit var compostLayout: LinearLayout
    private lateinit var landfillLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        recyclableLayout = view.findViewById(R.id.recyclable)
        compostLayout = view.findViewById(R.id.compost)
        landfillLayout = view.findViewById(R.id.landfill)

        // Set up click listeners
        setupClickListeners()
    }

    private fun setupClickListeners() {
        recyclableLayout.setOnClickListener {
            navigateToDetailScreen(WasteClassification.WasteType.RECYCLABLE.toString())
        }

        compostLayout.setOnClickListener {
            navigateToDetailScreen(WasteClassification.WasteType.COMPOST.toString())
        }

        landfillLayout.setOnClickListener {
            navigateToDetailScreen(WasteClassification.WasteType.LANDFILL.toString())
        }
    }

    private fun navigateToDetailScreen(categoryType: String) {
        val bundle = Bundle().apply {
            putString(GuideDetailFragment.ARG_CATEGORY_TYPE, categoryType)
        }
        findNavController().navigate(R.id.action_guidesFragment_to_guideDetailFragment, bundle)
    }
}