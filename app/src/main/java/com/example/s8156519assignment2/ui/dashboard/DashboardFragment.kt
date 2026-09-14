package com.example.s8156519assignment2.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.s8156519assignment2.R
import com.example.s8156519assignment2.data.model.Food
import com.example.s8156519assignment2.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment :
    Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null

    private val binding: FragmentDashboardBinding
        get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    private val foodAdapter = FoodAdapter { selectedFood ->
        openFoodDetails(selectedFood)
    }

    private val keypass: String
        get() = arguments?.getString("keypass").orEmpty()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDashboardBinding.bind(view)

        setupRecyclerView()
        setupRetryButton()
        observeDashboardState()

        if (viewModel.uiState.value is DashboardUiState.Idle) {
            viewModel.loadFoods(keypass)
        }
    }

    private fun setupRecyclerView() {
        binding.foodsRecyclerView.adapter = foodAdapter
        binding.foodsRecyclerView.setHasFixedSize(true)
    }

    private fun setupRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.loadFoods(keypass)
        }
    }

    private fun observeDashboardState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.uiState.collect { state ->
                    displayDashboardState(state)
                }
            }
        }
    }

    private fun displayDashboardState(
        state: DashboardUiState
    ) {
        when (state) {
            DashboardUiState.Idle -> {
                binding.dashboardProgressBar.isVisible = false
                binding.dashboardErrorContainer.isVisible = false
                binding.foodsRecyclerView.isVisible = false
            }

            DashboardUiState.Loading -> {
                binding.dashboardProgressBar.isVisible = true
                binding.dashboardErrorContainer.isVisible = false
                binding.foodsRecyclerView.isVisible = false
                binding.entityCountText.text =
                    "Loading food collection..."
            }

            is DashboardUiState.Success -> {
                binding.dashboardProgressBar.isVisible = false
                binding.dashboardErrorContainer.isVisible = false
                binding.foodsRecyclerView.isVisible = true

                binding.entityCountText.text =
                    "${state.entityTotal} dishes available"

                foodAdapter.submitList(state.foods)
            }

            is DashboardUiState.Error -> {
                binding.dashboardProgressBar.isVisible = false
                binding.dashboardErrorContainer.isVisible = true
                binding.foodsRecyclerView.isVisible = false
                binding.entityCountText.text =
                    "Food collection unavailable"

                binding.dashboardErrorText.text = state.message
            }
        }
    }

    private fun openFoodDetails(food: Food) {
        val foodDetails = bundleOf(
            "dishName" to food.dishName,
            "origin" to food.origin,
            "mainIngredient" to food.mainIngredient,
            "mealType" to food.mealType,
            "description" to food.description
        )

        findNavController().navigate(
            R.id.action_dashboardFragment_to_detailsFragment,
            foodDetails
        )
    }

    override fun onDestroyView() {
        binding.foodsRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }
}