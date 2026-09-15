package com.example.s8156519assignment2.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s8156519assignment2.R
import com.example.s8156519assignment2.data.model.Food
import com.example.s8156519assignment2.databinding.FragmentDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    private lateinit var foodAdapter: FoodAdapter
    private var allFoods: List<Food> = emptyList()
    private var keypass: String = ""

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDashboardBinding.bind(view)
        keypass = arguments?.getString("keypass").orEmpty()

        setupRecyclerView()
        setupSearch()
        setupButtons()
        observeDashboardState()

        viewModel.loadFoods(keypass)
    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter { food ->
            openFoodDetails(food)
        }

        binding.foodsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = foodAdapter
        }
    }

    private fun setupSearch() {
        binding.searchEditText.doAfterTextChanged { editable ->
            val query = editable?.toString()?.trim().orEmpty()

            val filteredFoods = if (query.isBlank()) {
                allFoods
            } else {
                allFoods.filter { food ->
                    food.dishName.contains(query, ignoreCase = true) ||
                            food.origin.contains(query, ignoreCase = true) ||
                            food.mainIngredient.contains(query, ignoreCase = true) ||
                            food.mealType.contains(query, ignoreCase = true)
                }
            }

            foodAdapter.submitList(filteredFoods)
            binding.entityCountText.text =
                "${filteredFoods.size} dishes available"
        }
    }

    private fun setupButtons() {
        binding.retryButton.setOnClickListener {
            viewModel.loadFoods(keypass)
        }

        binding.logoutButton.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to return to the Login page?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Logout") { _, _ ->
                val options = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()

                findNavController().navigate(
                    R.id.loginFragment,
                    null,
                    options
                )
            }
            .show()
    }

    private fun observeDashboardState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is DashboardUiState.Idle -> {
                            showLoading(false)
                        }

                        is DashboardUiState.Loading -> {
                            showLoading(true)
                        }

                        is DashboardUiState.Success -> {
                            showLoading(false)

                            allFoods = state.foods
                            foodAdapter.submitList(state.foods)

                            binding.entityCountText.text =
                                "${state.entityTotal} dishes available"

                            binding.foodsRecyclerView.isVisible = true
                            binding.errorContainer.isVisible = false
                        }

                        is DashboardUiState.Error -> {
                            showLoading(false)

                            binding.foodsRecyclerView.isVisible = false
                            binding.errorContainer.isVisible = true
                            binding.errorText.text = state.message
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.dashboardProgressBar.isVisible = isLoading

        if (isLoading) {
            binding.foodsRecyclerView.isVisible = false
            binding.errorContainer.isVisible = false
            binding.entityCountText.text = "Loading delicious dishes..."
        }
    }

    private fun openFoodDetails(food: Food) {
        val arguments = bundleOf(
            "dishName" to food.dishName,
            "origin" to food.origin,
            "mainIngredient" to food.mainIngredient,
            "mealType" to food.mealType,
            "description" to food.description
        )

        findNavController().navigate(
            R.id.action_dashboardFragment_to_detailsFragment,
            arguments
        )
    }

    override fun onDestroyView() {
        binding.foodsRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }
}