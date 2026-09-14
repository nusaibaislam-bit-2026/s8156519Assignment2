package com.example.s8156519assignment2.data.repository

import com.example.s8156519assignment2.data.model.DashboardResponse
import com.example.s8156519assignment2.data.model.LoginResponse

interface FoodRepository {

    suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse>

    suspend fun getFoods(
        keypass: String
    ): Result<DashboardResponse>
}