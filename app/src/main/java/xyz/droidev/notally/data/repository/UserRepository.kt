package xyz.droidev.notally.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import retrofit2.Response
import xyz.droidev.notally.data.api.UserApiService
import xyz.droidev.notally.data.local.prefrences.PreferenceManager
import xyz.droidev.notally.data.model.request.LoginBody
import xyz.droidev.notally.data.model.request.SignupBody
import xyz.droidev.notally.data.model.response.AuthResponse
import xyz.droidev.notally.util.NetworkResult
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val prefManager: PreferenceManager
) {

    private val _authResponseLiveData = MutableLiveData<NetworkResult<AuthResponse>>()
    val authResponseLiveData: LiveData<NetworkResult<AuthResponse>>
        get() = _authResponseLiveData

    suspend fun signup(
        SignupBody: SignupBody
    ) {
        _authResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApiService.signup(SignupBody)
        handleResponse(response)
    }

    suspend fun login( loginBody: LoginBody) {
        _authResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApiService.login(loginBody)
        handleResponse(response)
    }

    fun logout(){
        prefManager.clearPrefs()
    }

    fun isUserLoggedIn() = prefManager.isLoggedIn()

    private fun handleResponse(response: Response<AuthResponse>) {

        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            prefManager.apply {
                saveToken(body.accessToken)
                saveEmail(body.user.email)
                saveUsername(body.user.name)
                saveUserId(body.user.id)
                setLoggedIn(true)
            }
            _authResponseLiveData.postValue(NetworkResult.Success(body))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _authResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            _authResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    companion object{
        private const val TAG = "UserRepository"
    }

}