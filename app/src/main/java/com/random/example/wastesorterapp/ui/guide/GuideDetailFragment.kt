package com.random.example.wastesorterapp.ui.guide

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.random.example.wastesorterapp.R
import com.random.example.wastesorterapp.databinding.FragmentGuideDetailBinding
import com.random.example.wastesorterapp.model.WasteClassification

class GuideDetailFragment : Fragment() {
    private lateinit var binding: FragmentGuideDetailBinding

    companion object {
        const val ARG_CATEGORY_TYPE = "category_type"
    }

    private lateinit var categoryType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryType = it.getString(ARG_CATEGORY_TYPE, WasteClassification.WasteType.RECYCLABLE.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuideDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryDetails()

        setupExpandableSection(binding.disposalCard, binding.disposalArrow, binding.disposalContent,true)
        setupExpandableSection(binding.examplesCard, binding.examplesArrow, binding.examplesContent,false)
        setupExpandableSection(binding.warningCard, binding.warningArrow, binding.warningContent,false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    private fun setupCategoryDetails() {
        when (categoryType) {
            WasteClassification.WasteType.RECYCLABLE.toString() -> {
                binding.categoryTitle.text = getString(R.string.recyclable)
                binding.categoryIcon.setImageResource(R.drawable.recyclable)
                binding.headerLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.recyclable_dark))

                binding.disposalContent.text = getString(R.string.recyclable_disposal)

                binding.examplesContent.text = getString(R.string.recyclable_examples).trimIndent()

                binding.warningContent.text = getString(R.string.recyclable_warning).trimIndent()
            }
            WasteClassification.WasteType.COMPOST.toString() -> {
                binding.categoryTitle.text = getString(R.string.compost)
                binding.categoryIcon.setImageResource(R.drawable.leaf)
                binding.headerLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.compost_dark))

                binding.disposalContent.text = getString(R.string.compost_disposal)

                binding.examplesContent.text = getString(R.string.compost_example).trimIndent()

                binding.warningContent.text = getString(R.string.compost_warning).trimIndent()
            }
            WasteClassification.WasteType.LANDFILL.toString() -> {
                binding.categoryTitle.text = getString(R.string.landfill)
                binding.categoryIcon.setImageResource(R.drawable.trash)
                binding.headerLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.landfill_dark))

                binding.disposalContent.text = getString(R.string.landfill_disposal)

                binding.examplesContent.text = getString(R.string.landfill_example).trimIndent()

                binding.warningContent.text = getString(R.string.landfill_warning).trimIndent()
            }
        }
    }

    private fun setupExpandableSection(card: CardView, arrow: ImageView, content: TextView, initiallyExpanded: Boolean) {
        // Set initial state
        content.visibility = if (initiallyExpanded) View.VISIBLE else View.GONE
        arrow.rotation = if (initiallyExpanded) 180F else 0f
        val animationDuration = 200L

        // Measure the full height of the content when visible
        content.post {
            // Store the original height to use in animations
            val contentHeight = content.measuredHeight

            // If initially not expanded, set height to 0
            if (!initiallyExpanded) {
                content.layoutParams.height = 0
            }

            // Set up click listener for expand/collapse
            card.setOnClickListener {
                if (content.visibility == View.VISIBLE) {
                    // Collapse animation
                    val animator = ValueAnimator.ofInt(contentHeight, 0)
                    animator.addUpdateListener { valueAnimator ->
                        content.layoutParams.height = valueAnimator.animatedValue as Int
                        content.requestLayout()
                    }
                    animator.doOnEnd {
                        content.visibility = View.GONE
                    }
                    animator.interpolator = AccelerateDecelerateInterpolator()
                    animator.duration = animationDuration
                    animator.start()

                    // Rotate arrow animation
                    arrow.animate().rotation(0f).setDuration(animationDuration).start()
                } else {
                    // Make visible but with height 0 for animation
                    content.visibility = View.VISIBLE
                    content.layoutParams.height = 0

                    // Expand animation
                    val animator = ValueAnimator.ofInt(0, contentHeight)
                    animator.addUpdateListener { valueAnimator ->
                        content.layoutParams.height = valueAnimator.animatedValue as Int
                        content.requestLayout()
                    }
                    animator.doOnEnd {
                        // Set to WRAP_CONTENT after animation completes
                        content.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                    animator.interpolator = AccelerateDecelerateInterpolator()
                    animator.duration = animationDuration
                    animator.start()

                    // Rotate arrow animation
                    arrow.animate().rotation(180f).setDuration(animationDuration).start()
                }
            }
        }
    }
}