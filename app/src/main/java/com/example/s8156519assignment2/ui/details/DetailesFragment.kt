package com.example.s8156519assignment2.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.s8156519assignment2.R
import com.example.s8156519assignment2.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null

    private val binding: FragmentDetailsBinding
        get() = _binding!!

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsBinding.bind(view)

        setupBackButton()
        displaySelectedFood()
    }

    private fun setupBackButton() {
        binding.detailsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun displaySelectedFood() {
        val dishName =
            arguments?.getString("dishName").orEmpty()

        val origin =
            arguments?.getString("origin").orEmpty()

        val mainIngredient =
            arguments?.getString("mainIngredient").orEmpty()

        val mealType =
            arguments?.getString("mealType").orEmpty()

        val description =
            arguments?.getString("description").orEmpty()

        binding.detailsDishNameText.text = dishName
        binding.detailsOriginText.text = "Origin: $origin"
        binding.detailsIngredientText.text =
            "Main ingredient: $mainIngredient"
        binding.detailsMealTypeText.text =
            "Meal type: $mealType"
        binding.detailsDescriptionText.text = description

        binding.detailsFoodIconText.text =
            getFoodEmoji(dishName)

        binding.detailsFoodIconText.contentDescription =
            "$dishName illustration"
    }

    private fun getFoodEmoji(dishName: String): String {
        return when (dishName.lowercase()) {
            "sushi" -> "🍣"
            "pizza" -> "🍕"
            "tacos" -> "🌮"
            "croissant" -> "🥐"
            "pad thai" -> "🍜"
            "hamburger" -> "🍔"
            "curry" -> "🍛"
            else -> "🍽️"
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}