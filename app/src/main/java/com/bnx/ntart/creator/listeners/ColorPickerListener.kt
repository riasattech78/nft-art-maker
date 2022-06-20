package com.bnx.ntart.creator.listeners

import android.view.View
import com.bnx.ntart.creator.models.ColorPalette

interface ColorPickerListener {
    fun onColorTapped(colorTapped: Int, view: View)
    fun onColorLongTapped(paletteId: Int, colorTapped: Int, view: View)
    fun onColorAdded(colorPalette: ColorPalette)
}