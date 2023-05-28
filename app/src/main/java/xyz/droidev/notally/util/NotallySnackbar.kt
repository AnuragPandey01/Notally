package xyz.droidev.notally.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import xyz.droidev.notally.R
import xyz.droidev.notally.databinding.SnackbarBinding


fun FragmentActivity.makeNotallySnackbar(level: SnackbarLevel, view: View, message: String, title: String?): Snackbar {

    val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)


    val binding = SnackbarBinding.inflate(layoutInflater)

    val color = ContextCompat.getColor(this, getColorFromLevel(level))

    binding.apply {
        tvTitle.text = title ?: level.name.capitalized()
        tvTitle.setTextColor(color)
        tvMsg.text = message
        card.strokeColor = color
    }


    val snackbarLayout = snackbar.view as SnackbarLayout
    snackbarLayout.setPadding(0, 0, 0, 0)
    snackbarLayout.addView(binding.root, 0)
    snackbarLayout.layoutParams.height = 230

    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

    return snackbar
}

private fun getColorFromLevel(level: SnackbarLevel): Int {
    return when(level){
        SnackbarLevel.SUCCESS -> R.color.color_3
        SnackbarLevel.ERROR -> R.color.color_1
        SnackbarLevel.INFO -> R.color.on_background
    }
}

enum class SnackbarLevel{
    SUCCESS,
    ERROR,
    INFO
}