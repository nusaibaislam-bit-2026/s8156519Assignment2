package com.example.s8156519assignment2.data.repository

import com.example.s8156519assignment2.data.model.DashboardResponse
import com.example.s8156519assignment2.data.model.LoginRequest
import com.example.s8156519assignment2.data.model.LoginResponse
import com.example.s8156519assignment2.data.remote.FoodApiService
import java.io.IOException
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val apiService: FoodApiService
) : FoodRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {
        return try {
            val response = apiService.login(
                LoginRequest(
                    username = username,
                    password = password
                )
            )

            if (response.isSuccessful) {
                val loginResponse = response.body()

                if (loginResponse != null) {
                    Result.success(loginResponse)
                } else {
                    Result.failure(
                        Exception("The server returned an empty login response.")
                    )
                }
            } else {
                val message = when (response.code()) {
                    400, 401, 403 -> "Incorrect username or password."
                    else -> "Login failed. Server error ${response.code()}."
                }

                Result.failure(Exception(message))
            }
        } catch (exception: IOException) {
            Result.failure(
                Exception(
                    "Unable to connect. Check your internet connection.",
                    exception
                )
            )
        } catch (exception: Exception) {
            Result.failure(
                Exception(
                    exception.message ?: "An unexpected login error occurred.",
                    exception
                )
            )
        }
    }

    override suspend fun getFoods(
        keypass: String
    ): Result<DashboardResponse> {
        return try {
            val response = apiService.getFoods(keypass)

            if (response.isSuccessful) {
                val dashboardResponse = response.body()

                if (dashboardResponse != null) {
                    Result.success(dashboardResponse)
                } else {
                    Result.failure(
                        Exception("The server returned an empty food response.")
                    )
                }
            } else {
                Result.failure(
                    Exception(
                        "Unable to load foods. Server error ${response.code()}."
                    )
                )
            }
        } catch (exception: IOException) {
            Result.failure(
                Exception(
                    "Unable to connect. Check your internet connection.",
                    exception
                )
            )
        } catch (exception: Exception) {
            Result.failure(
                Exception(
                    exception.message ?: "An unexpected dashboard error occurred.",
                    exception
                )
            )
        }
    }
}
