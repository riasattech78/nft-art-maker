package com.bnx.ntart.creator.listeners

import com.bnx.ntart.creator.models.ColorPalette

interface ColorPalettesFragmentListener {
    fun onColorPaletteTapped(selectedColorPalette: ColorPalette)
}