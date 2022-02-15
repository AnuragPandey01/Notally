package com.nekodev.notally.util

object colorPicker {
    private var i = 0
    private val colorList = arrayOf("#E5B9AF", "#C3C4E5", "#C3EFC0", "#F8FFC6")
    fun getBackgroundColor(): String{
        if(i ==4){ i =0 }
        i++
        return colorList[i -1]
    }
}