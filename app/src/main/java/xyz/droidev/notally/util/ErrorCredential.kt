package xyz.droidev.notally.util

sealed class ErrorCredential( val message: String) {

    class Email( message: String): ErrorCredential(message)
    class Password( message: String): ErrorCredential(message)
    class UserName( message: String): ErrorCredential(message)
    object None: ErrorCredential("")
}