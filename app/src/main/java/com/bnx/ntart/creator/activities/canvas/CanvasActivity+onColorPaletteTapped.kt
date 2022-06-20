package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.creator.adapters.ColorPickerAdapter
import com.bnx.ntart.creator.models.ColorPalette

fun CanvasActivity.extendedOnColorPaletteTapped(selectedColorPalette: ColorPalette) {
    binding.activityCanvasColorPickerRecyclerView.adapter = ColorPickerAdapter(selectedColorPalette, this)
}