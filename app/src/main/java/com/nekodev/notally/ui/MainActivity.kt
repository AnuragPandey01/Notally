package com.nekodev.notally.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nekodev.notally.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Notally)
        setContentView(R.layout.activity_main)
    }
}