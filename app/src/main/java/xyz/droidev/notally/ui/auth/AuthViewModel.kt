package xyz.droidev.notally.ui.auth

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.droidev.notally.data.model.request.LoginBody
import xyz.droidev.notally.data.model.request.SignupBody
import xyz.droidev.notally.data.model.response.AuthResponse
import xyz.droidev.notally.data.repository.UserRepository
import xyz.droidev.notally.util.ErrorCredential
import xyz.droidev.notally.util.NetworkResult
import xyz.droidev.notally.util.isValidEmail
import xyz.droidev.notally.util.isValidPassword
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val  userRepository: UserRepository): ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<AuthResponse>>
        get() = userRepository.authResponseLiveData

    fun signup(
        SignupBody: SignupBody
    ) {
        viewModelScope.launch {
            userRepository.signup(SignupBody)
        }
    }

    fun login(
        loginBody: LoginBody
    ) {
        viewModelScope.launch {
            userRepository.login(loginBody)
        }
    }

    fun isUserLoggedIn() = userRepository.isUserLoggedIn()

    fun validateCredentials(
        emailAddress: String,
        userName: String = "",
        password: String,
        isLogin: Boolean
    ) : ErrorCredential {

        var result : ErrorCredential = ErrorCredential.None

        if(TextUtils.isEmpty(userName) && !isLogin  ){
            result = ErrorCredential.UserName("Name can't be empty")
        }
        else if(!emailAddress.isValidEmail()){
            result = ErrorCredential.Email("Invalid Email Address")
        }
        else if(!password.isValidPassword()){
            result = ErrorCredential.Password("Password must be at least 6 characters")
        }
        return result
    }

}