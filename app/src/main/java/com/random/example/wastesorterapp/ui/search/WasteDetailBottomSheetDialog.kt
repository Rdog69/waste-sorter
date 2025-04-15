package com.random.example.wastesorterapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.random.example.wastesorterapp.R
import com.random.example.wastesorterapp.databinding.BottomSheetWasteDetailBinding
import com.random.example.wastesorterapp.model.WasteClassification.WasteType
import com.random.example.wastesorterapp.model.WasteItem

class WasteDetailBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetWasteDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWasteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the selected waste item
        viewModel.selectedWasteItem.observe(viewLifecycleOwner) { wasteItem ->
            wasteItem?.let { updateUI(it) }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.NoBackgroundDialogTheme
    }

    private fun updateUI(wasteItem: WasteItem) {
        binding.tvItemName.text = wasteItem.name
        binding.tvItemDescription.text = wasteItem.description
        binding.tvDisposalInstructions.text = wasteItem.instructions

        // Set the category icon and text
        val context = requireContext()
        binding.tvItemCategory.text = wasteItem.wasteType.name.capitalize()

        when (wasteItem.wasteType) {
            WasteType.RECYCLABLE -> {
                binding.ivCategoryIcon.setImageResource(R.drawable.recyclable)
                val background = ContextCompat.getDrawable(context, R.drawable.bg_tv)?.mutate()
                if (background != null) {
                    DrawableCompat.setTint(background, ContextCompat.getColor(context, R.color.recyclable))
                    binding.tvItemCategory.background = background
                }
            }
            WasteType.COMPOST -> {
                binding.ivCategoryIcon.setImageResource(R.drawable.leaf)
                val background = ContextCompat.getDrawable(context, R.drawable.bg_tv)?.mutate()
                if (background != null) {
                    DrawableCompat.setTint(background, ContextCompat.getColor(context, R.color.compost))
                    binding.tvItemCategory.background = background
                }
            }
            WasteType.LANDFILL -> {
                binding.ivCategoryIcon.setImageResource(R.drawable.trash)
                val background = ContextCompat.getDrawable(context, R.drawable.bg_tv)?.mutate()
                if (background != null) {
                    DrawableCompat.setTint(background, ContextCompat.getColor(context, R.color.landfill))
                    binding.tvItemCategory.background = background
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): WasteDetailBottomSheetDialog {
            return WasteDetailBottomSheetDialog()
        }
    }
}