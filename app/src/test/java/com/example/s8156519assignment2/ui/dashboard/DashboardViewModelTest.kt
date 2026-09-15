package com.example.s8156519assignment2.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.s8156519assignment2.data.model.DashboardResponse
import com.example.s8156519assignment2.data.model.Food
import com.example.s8156519assignment2.data.model.LoginResponse
import com.example.s8156519assignment2.data.repository.FoodRepository
import com.example.s8156519assignment2.ui.login.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeDashboardRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        fakeRepository = FakeDashboardRepository()
        viewModel = DashboardViewModel(fakeRepository)
    }

    @Test
    fun `successful dashboard request returns food list`() = runTest {
        val foods = listOf(
            Food(
                dishName = "Sushi",
                origin = "Japan",
                mainIngredient = "Rice",
                mealType = "Lunch/Dinner",
                description = "Japanese rice dish"
            ),
            Food(
                dishName = "Pizza",
                origin = "Italy",
                mainIngredient = "Dough",
                mealType = "Lunch/Dinner",
                description = "Italian dish"
            )
        )

        fakeRepository.dashboardResult = Result.success(
            DashboardResponse(
                entities = foods,
                entityTotal = foods.size
            )
        )

        viewModel.loadFoods("FOOOD")

        val state = viewModel.uiState.value

        assertTrue(state is DashboardUiState.Success)

        state as DashboardUiState.Success

        assertEquals(2, state.foods.size)
        assertEquals("Sushi", state.foods.first().dishName)
        assertEquals(2, state.entityTotal)
    }

    @Test
    fun `failed dashboard request returns error state`() = runTest {
        fakeRepository.dashboardResult = Result.failure(
            Exception("Unable to load foods")
        )

        viewModel.loadFoods("FOOOD")

        val state = viewModel.uiState.value

        assertTrue(state is DashboardUiState.Error)
    }
}

private class FakeDashboardRepository : FoodRepository {

    var dashboardResult: Result<DashboardResponse> =
        Result.failure(Exception("No test response configured"))

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {
        return Result.failure(
            Exception("Login is not used in dashboard tests")
        )
    }

    override suspend fun getFoods(
        keypass: String
    ): Result<DashboardResponse> {
        return dashboardResult
    }
}