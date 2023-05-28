package xyz.droidev.notally.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.droidev.notally.data.model.request.LoginBody
import xyz.droidev.notally.databinding.FragmentLoginBinding
import xyz.droidev.notally.util.ErrorCredential
import xyz.droidev.notally.util.NetworkResult
import xyz.droidev.notally.util.SnackbarLevel
import xyz.droidev.notally.util.makeNotallySnackbar

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel : AuthViewModel by viewModels()
    private var _binding: FragmentLoginBinding?  = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setOnClickListeners()
        bindObservers()
    }

    private fun bindObservers(){

        viewModel.userResponseLiveData.observe(viewLifecycleOwner){ networkResult ->
            when(networkResult){

                is NetworkResult.Loading ->{
                    binding.lottieLoading.visibility = View.VISIBLE
                    binding.btnLogin.visibility = View.GONE
                }

                is NetworkResult.Success ->{
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNotesFragment())
                }

                is NetworkResult.Error ->{
                    binding.lottieLoading.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE

                    requireActivity()
                        .makeNotallySnackbar(SnackbarLevel.ERROR ,binding.root,networkResult.message!!,"Error")
                        .setAnchorView(binding.tvNotally)
                        .show()

                }
            }
        }

    }

    private fun setOnClickListeners() {

        binding.tvForgetPassword.setOnClickListener {
            //TODO: implement forget password
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val error = viewModel.validateCredentials(
                emailAddress = email,
                password = password,
                isLogin = true
            )

            if (error is ErrorCredential.None) {
                viewModel.login(LoginBody(email, password))
            } else {
                handleError(error)
            }

        }
    }

    private fun handleError(error: ErrorCredential) {

        when(error){
            is ErrorCredential.Email ->{
                binding.etEmailLayout.error = error.message
            }
            is ErrorCredential.Password ->{
                binding.etPasswordLayout.error = error.message
            }
            else ->{
                // do nothing
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}