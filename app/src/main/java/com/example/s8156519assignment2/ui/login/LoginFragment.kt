package com.example.s8156519assignment2.ui.login

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
import com.example.s8156519assignment2.databinding.FragmentLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private var navigationHandled = false

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        setupLoginButton()
        setupTermsButton()
        observeLoginState()
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text
                ?.toString()
                ?.trim()
                .orEmpty()

            val password = binding.passwordEditText.text
                ?.toString()
                .orEmpty()

            binding.usernameInputLayout.error = null
            binding.passwordInputLayout.error = null
            binding.loginErrorText.isVisible = false

            when {
                username.isBlank() -> {
                    binding.usernameInputLayout.error =
                        "Please enter your student ID"
                }

                password.isBlank() -> {
                    binding.passwordInputLayout.error =
                        "Please enter your password"
                }

                else -> {
                    viewModel.login(username, password)
                }
            }
        }
    }

    private fun setupTermsButton() {
        binding.termsText.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Terms and Conditions")
                .setMessage(
                    "Food Explorer is an educational application created for " +
                            "the NIT3213 final assignment.\n\n" +
                            "Your credentials are sent securely to the university API " +
                            "for authentication. The application does not permanently " +
                            "store your username or password.\n\n" +
                            "Food information displayed in this application is provided " +
                            "by the university API."
                )
                .setPositiveButton("I understand", null)
                .show()
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUiState.Idle -> {
                            showLoading(false)
                            binding.loginErrorText.isVisible = false
                        }

                        is LoginUiState.Loading -> {
                            showLoading(true)
                            binding.loginErrorText.isVisible = false
                        }

                        is LoginUiState.Success -> {
                            showLoading(false)

                            if (!navigationHandled) {
                                navigationHandled = true

                                val arguments = bundleOf(
                                    "keypass" to state.keypass
                                )

                                findNavController().navigate(
                                    R.id.action_loginFragment_to_dashboardFragment,
                                    arguments
                                )
                            }
                        }

                        is LoginUiState.Error -> {
                            showLoading(false)
                            binding.loginErrorText.text = state.message
                            binding.loginErrorText.isVisible = true
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginProgressBar.isVisible = isLoading
        binding.loginButton.isEnabled = !isLoading
        binding.usernameEditText.isEnabled = !isLoading
        binding.passwordEditText.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}