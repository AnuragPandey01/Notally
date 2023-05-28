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
import xyz.droidev.notally.util.*

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

    override fun onStart() {
        if(viewModel.isUserLoggedIn()){
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToNotesFragment())
        }
        super.onStart()
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

                        requireActivity()
                            .makeNotallySnackbar(SnackbarLevel.ERROR ,binding.root,networkResult.message!!,"Error")
                            .setAnchorView(binding.tvNotally)
                            .show()
                    }
                }
            }
    }

    private fun setOnClickListeners(){

        binding.btnLogin.setOnClickListener {
           findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }

        binding.btnSignup.setOnClickListener {

            requireContext().hideKeyboard(binding.btnSignup)

            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val error = viewModel.validateCredentials(email,name,password,false)

            if(error is ErrorCredential.None && binding.cbTerms.isChecked){

                viewModel.signup(SignupBody(name,email,password))

            }else if(error is ErrorCredential.None && binding.cbTerms.isChecked.not()){

                requireActivity()
                    .makeNotallySnackbar(SnackbarLevel.ERROR ,binding.root,"Please Accept terms & conditions","Error")
                    .setAnchorView(binding.tvNotally)
                    .show()

            }else{
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