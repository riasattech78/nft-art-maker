package com.bnx.ntart.creator.listeners

interface ColorPickerFragmentListener {
    fun onDoneButtonPressed(selectedColor: Int, isColorPaletteMode: Boolean = false)
}