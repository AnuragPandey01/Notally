package xyz.droidev.notally.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.droidev.notally.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Notally)
        setContentView(R.layout.activity_main)
    }
}