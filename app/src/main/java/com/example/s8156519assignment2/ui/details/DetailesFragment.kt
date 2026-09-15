package com.example.s8156519assignment2.ui.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.s8156519assignment2.R
import com.example.s8156519assignment2.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private var dishName = ""
    private var origin = ""
    private var mainIngredient = ""
    private var mealType = ""
    private var description = ""

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsBinding.bind(view)

        readFoodArguments()
        displayFoodInformation()
        setupButtons()
    }

    private fun readFoodArguments() {
        dishName = arguments?.getString("dishName").orEmpty()
        origin = arguments?.getString("origin").orEmpty()
        mainIngredient = arguments?.getString("mainIngredient").orEmpty()
        mealType = arguments?.getString("mealType").orEmpty()
        description = arguments?.getString("description").orEmpty()
    }

    private fun displayFoodInformation() {
        binding.foodIconText.text = foodEmoji(dishName)
        binding.dishNameText.text = dishName
        binding.originHeaderText.text = "🌍 From $origin"
        binding.originText.text = origin
        binding.mainIngredientText.text = mainIngredient
        binding.mealTypeText.text = mealType
        binding.descriptionText.text = description
    }

    private fun setupButtons() {
        binding.detailsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.returnButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareButton.setOnClickListener {
            shareFood()
        }

        binding.copyButton.setOnClickListener {
            copyFoodInformation()
        }
    }

    private fun createFoodText(): String {
        return """
            $dishName
            
            Origin: $origin
            Main ingredient: $mainIngredient
            Meal type: $mealType
            
            $description
            
            Shared from Food Explorer
        """.trimIndent()
    }

    private fun shareFood() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, dishName)
            putExtra(Intent.EXTRA_TEXT, createFoodText())
        }

        startActivity(
            Intent.createChooser(
                shareIntent,
                "Share $dishName"
            )
        )
    }

    private fun copyFoodInformation() {
        val clipboardManager = requireContext().getSystemService(
            Context.CLIPBOARD_SERVICE
        ) as ClipboardManager

        val clip = ClipData.newPlainText(
            dishName,
            createFoodText()
        )

        clipboardManager.setPrimaryClip(clip)

        Toast.makeText(
            requireContext(),
            "$dishName information copied",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun foodEmoji(name: String): String {
        return when (name.lowercase()) {
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