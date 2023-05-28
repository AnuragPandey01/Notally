package xyz.droidev.notally.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.droidev.notally.data.model.request.SignupBody
import xyz.droidev.notally.databinding.FragmentSignupBinding
import xyz.droidev.notally.util.ErrorCredential
import xyz.droidev.notally.util.NetworkResult
import xyz.droidev.notally.util.hideKeyboard
import xyz.droidev.notally.util.showToast

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val viewModel: AuthViewModel  by viewModels()
    private var _binding: FragmentSignupBinding?  = null
    private val binding: FragmentSignupBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setOnClickListeners()
        bindObservers()
    }

    private fun bindObservers(){

            viewModel.userResponseLiveData.observe(viewLifecycleOwner) { networkResult ->
                when (networkResult) {

                    is NetworkResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                        binding.btnSignup.visibility = View.GONE
                    }

                    is NetworkResult.Success -> {
                        findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToNotesFragment())
                    }

                    is NetworkResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnSignup.visibility = View.VISIBLE
                        requireContext().showToast(networkResult.message!!)
                    }
                }
            }
    }

    private fun setOnClickListeners(){

        binding.btnLogin.setOnClickListener {
            //TODO: Navigate to login fragment
        }

        binding.btnSignup.setOnClickListener {

            requireContext().hideKeyboard(binding.btnSignup)

            if(binding.cbTerms.isChecked.not()){
                requireContext().showToast("Please accept terms and conditions")
                return@setOnClickListener
            }

            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val error = viewModel.validateCredentials(email,name,password,false)

            if(error is ErrorCredential.None){
                viewModel.signup(SignupBody(name,email,password))
            }
            else{
                handleErrorCredential(error)
            }
        }
    }

    private fun handleErrorCredential(errorCredential: ErrorCredential){
        when(errorCredential){

            is ErrorCredential.UserName -> {
                binding.etName.error = errorCredential.message
            }
            is ErrorCredential.Email -> {
                binding.etEmail.error = errorCredential.message
            }
            is ErrorCredential.Password -> {
                binding.etPassword.error = errorCredential.message
            }
            is ErrorCredential.None -> {
                // Do nothing
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}