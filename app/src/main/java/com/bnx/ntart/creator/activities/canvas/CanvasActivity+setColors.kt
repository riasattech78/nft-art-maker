package com.bnx.ntart.creator.activities.canvas

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.bnx.ntart.R

fun setColors(context:Context) {
    setPrimaryPixelColor(ContextCompat.getColor(context, R.color.primary))
    setSecondaryPixelColor(Color.BLUE)
}