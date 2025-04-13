package com.random.example.wastesorterapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.random.example.wastesorterapp.WasteSorterApplication
import com.random.example.wastesorterapp.data.repository.WasteItemRepository
import com.random.example.wastesorterapp.databinding.FragmentSearchBinding
import com.random.example.wastesorterapp.model.WasteClassification.WasteType

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WasteItemAdapter

    private val viewModel: SearchViewModel by viewModels {
        val wasteItemDao = (requireActivity().application as WasteSorterApplication)
            .database.wasteItemDao()
        val repository = WasteItemRepository(wasteItemDao)
        SearchViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()
        setupFilterChips()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = WasteItemAdapter { wasteItem ->
            // Set the wasteItem in ViewModel and show details
            viewModel.selectWasteItem(wasteItem)
            showWasteItemDetails()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
    }


    private fun setupSearchInput() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setSearchQuery(text.toString())
        }
    }

    private fun setupFilterChips() {
        binding.chipAll.setOnClickListener {
            viewModel.setFilterType(null)
        }

        binding.chipRecyclable.setOnClickListener {
            viewModel.setFilterType(WasteType.RECYCLABLE)
        }

        binding.chipCompost.setOnClickListener {
            viewModel.setFilterType(WasteType.COMPOST)
        }

        binding.chipLandfill.setOnClickListener {
            viewModel.setFilterType(WasteType.LANDFILL)
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { wasteItems ->
            adapter.submitList(wasteItems)

            // Show empty view if no results
            if (wasteItems.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun showWasteItemDetails() {
        // No need to pass ID since we already have the item in ViewModel
        val detailBottomSheet = WasteDetailBottomSheetDialog.newInstance()
        detailBottomSheet.show(childFragmentManager, "WasteDetailBottomSheet")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}