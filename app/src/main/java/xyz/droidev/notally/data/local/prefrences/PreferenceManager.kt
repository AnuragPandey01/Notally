package xyz.droidev.notally.data.local.prefrences

import android.content.Context

class PreferenceManager(
    context: Context
) {
    companion object{
        const val PREF_NAME = "notally_pref"
        const val PREF_TOKEN = "token"
        const val PREF_EMAIL = "email"
        const val PREF_USERNAME = "username"
        const val USER_ID = "user_id"
        const val IS_LOGGED_IN = "is_logged_in"
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String){
        sharedPreferences.edit().putString(PREF_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(PREF_TOKEN, null)
    }

    fun saveEmail(email: String){
        sharedPreferences.edit().putString(PREF_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(PREF_EMAIL, null)
    }

    fun saveUsername(username: String){
        sharedPreferences.edit().putString(PREF_USERNAME, username).apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(PREF_USERNAME, null)
    }

    fun saveUserId(userId: String){
        sharedPreferences.edit().putString(USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID, null)
    }

    fun setLoggedIn(isLoggedIn: Boolean){
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun clearPrefs(){
        sharedPreferences.edit().clear().apply()
    }


}