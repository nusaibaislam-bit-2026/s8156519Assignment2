package com.example.s8156519assignment2.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.s8156519assignment2.data.model.DashboardResponse
import com.example.s8156519assignment2.data.model.LoginResponse
import com.example.s8156519assignment2.data.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeFoodRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        fakeRepository = FakeFoodRepository()
        viewModel = LoginViewModel(fakeRepository)
    }

    @Test
    fun `successful login returns keypass`() = runTest {
        fakeRepository.loginResult = Result.success(
            LoginResponse(keypass = "FOOOD")
        )

        viewModel.login(
            username = "8156519",
            password = "Nusaiba Islam"
        )

        val state = viewModel.uiState.value

        assertTrue(state is LoginUiState.Success)
        assertEquals(
            "FOOOD",
            (state as LoginUiState.Success).keypass
        )
    }

    @Test
    fun `failed login returns error state`() = runTest {
        fakeRepository.loginResult = Result.failure(
            Exception("Invalid username or password")
        )

        viewModel.login(
            username = "wrong",
            password = "wrong"
        )

        val state = viewModel.uiState.value

        assertTrue(state is LoginUiState.Error)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

private class FakeFoodRepository : FoodRepository {

    var loginResult: Result<LoginResponse> = Result.success(
        LoginResponse(keypass = "FOOOD")
    )

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {
        return loginResult
    }

    override suspend fun getFoods(
        keypass: String
    ): Result<DashboardResponse> {
        return Result.failure(
            Exception("Dashboard is not used in login tests")
        )
    }
}