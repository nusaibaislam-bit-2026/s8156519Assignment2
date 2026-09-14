package com.example.s8156519assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.s8156519assignment2.data.model.Food
import com.example.s8156519assignment2.databinding.ItemFoodBinding

class FoodAdapter(
    private val onFoodClick: (Food) -> Unit
) : ListAdapter<Food, FoodAdapter.FoodViewHolder>(
    FoodDiffCallback
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FoodViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class FoodViewHolder(
        private val binding: ItemFoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.dishNameText.text = food.dishName
            binding.originText.text = "Origin: ${food.origin}"
            binding.ingredientText.text =
                "Main ingredient: ${food.mainIngredient}"
            binding.mealTypeText.text =
                "Meal type: ${food.mealType}"

            binding.foodIconText.text =
                getFoodEmoji(food.dishName)

            binding.foodIconText.contentDescription =
                "${food.dishName} illustration"

            binding.root.setOnClickListener {
                onFoodClick(food)
            }

            binding.detailsButton.setOnClickListener {
                onFoodClick(food)
            }
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
    }

    private object FoodDiffCallback :
        DiffUtil.ItemCallback<Food>() {

        override fun areItemsTheSame(
            oldItem: Food,
            newItem: Food
        ): Boolean {
            return oldItem.dishName == newItem.dishName
        }

        override fun areContentsTheSame(
            oldItem: Food,
            newItem: Food
        ): Boolean {
            return oldItem == newItem
        }
    }
}